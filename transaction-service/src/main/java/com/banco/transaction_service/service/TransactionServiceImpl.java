package com.banco.transaction_service.service;

import com.banco.transaction_service.client.AccountClient;
import com.banco.transaction_service.dto.CuentaDto;
import com.banco.transaction_service.dto.OperacionRequest;
import com.banco.transaction_service.dto.TransaccionResponse;
import com.banco.transaction_service.dto.TransferenciaRequest;
import com.banco.transaction_service.entity.Transaccion;
import com.banco.transaction_service.enums.EstadoTransaccion;
import com.banco.transaction_service.enums.TipoTransaccion;
import com.banco.transaction_service.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final TransaccionRepository transaccionRepository;
    private final AccountClient accountClient;

    @Override
    @Transactional
    public TransaccionResponse transferir(TransferenciaRequest request) {

        CuentaDto origen;
        CuentaDto destino;

        try {
            origen = accountClient.obtenerPorNumeroCuenta(request.cuentaOrigen());
        } catch (feign.FeignException e) {
            throw new RuntimeException("La cuenta de origen número " + request.cuentaOrigen() + " no existe.");
        }

        try {
            destino = accountClient.obtenerPorNumeroCuenta(request.cuentaDestino());
        } catch (feign.FeignException e) {
            throw new RuntimeException("La cuenta de destino número " + request.cuentaDestino() + " no existe.");
        }

        if (request.cuentaOrigen().equals(request.cuentaDestino())) {
            throw new RuntimeException("No se puede realizar una transferencia a la misma cuenta de origen.");
        }

        if (!origen.activo()) {
            throw new RuntimeException("La cuenta de origen se encuentra bloqueada.");
        }

        if (origen.saldo().compareTo(request.monto()) < 0) {
            throw new RuntimeException("Fondos insuficientes en la cuenta de origen.");
        }

        if (!destino.activo()) {
            throw new RuntimeException("La cuenta de destino se encuentra inactiva o bloqueada.");
        }

        BigDecimal nuevoSaldoOrigen = origen.saldo().subtract(request.monto());
        BigDecimal nuevoSaldoDestino = destino.saldo().add(request.monto());

        //impactar los saldos en el account-service vía Feign
        accountClient.actualizarSaldo(origen.id(), nuevoSaldoOrigen);
        accountClient.actualizarSaldo(destino.id(), nuevoSaldoDestino);

        //registrar el éxito de la transferencia en el historial
        Transaccion transaccion = Transaccion.builder()
                .cuentaOrigen(request.cuentaOrigen())
                .cuentaDestino(request.cuentaDestino())
                .monto(request.monto())
                .tipo(TipoTransaccion.TRANSFERENCIA)
                .estado(EstadoTransaccion.COMPLETADA)
                .build();

        Transaccion guardada = transaccionRepository.save(transaccion);
        return mapToResponse(guardada);
    }

    @Override
    @Transactional
    public TransaccionResponse depositar(OperacionRequest request) {
        CuentaDto destino;
        try {
            destino = accountClient.obtenerPorNumeroCuenta(request.cuenta());
        } catch (feign.FeignException e) {
            throw new RuntimeException("La cuenta número " + request.cuenta() + " no existe.");
        }

        if (!destino.activo()) {
            throw new RuntimeException("No se puede depositar en una cuenta inactiva.");
        }

        BigDecimal nuevoSaldo = destino.saldo().add(request.monto());
        accountClient.actualizarSaldo(destino.id(), nuevoSaldo);

        Transaccion transaccion = Transaccion.builder()
                .cuentaOrigen(null) // deposito no tiene cuenta origen
                .cuentaDestino(request.cuenta())
                .monto(request.monto())
                .tipo(TipoTransaccion.DEPOSITO)
                .estado(EstadoTransaccion.COMPLETADA)
                .build();

        return mapToResponse(transaccionRepository.save(transaccion));
    }

    @Override
    @Transactional
    public TransaccionResponse retirar(OperacionRequest request) {
        CuentaDto origen;

        try {
            origen = accountClient.obtenerPorNumeroCuenta(request.cuenta());
        } catch (feign.FeignException e) {
            throw new RuntimeException("La cuenta número " + request.cuenta() + " no existe.");
        }

        if (!origen.activo()) {
            throw new RuntimeException("Cuenta inactiva.");
        }
        if (origen.saldo().compareTo(request.monto()) < 0) {
            throw new RuntimeException("Fondos insuficientes para el retiro.");
        }

        BigDecimal nuevoSaldo = origen.saldo().subtract(request.monto());
        accountClient.actualizarSaldo(origen.id(), nuevoSaldo);

        Transaccion transaccion = Transaccion.builder()
                .cuentaOrigen(request.cuenta())
                .cuentaDestino(null) //el retiro no va a otra cuenta
                .monto(request.monto())
                .tipo(TipoTransaccion.RETIRO)
                .estado(EstadoTransaccion.COMPLETADA)
                .build();

        return mapToResponse(transaccionRepository.save(transaccion));
    }

    private TransaccionResponse mapToResponse(Transaccion t) {
        return new TransaccionResponse(
                t.getId(), t.getCuentaOrigen(), t.getCuentaDestino(),
                t.getMonto(), t.getTipo(), t.getEstado(), t.getFechaTransaccion()
        );
    }
}
