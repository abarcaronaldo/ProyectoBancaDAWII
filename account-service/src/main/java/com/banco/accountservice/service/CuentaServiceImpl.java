package com.banco.accountservice.service;

import com.banco.accountservice.client.CustomerClient;
import com.banco.accountservice.dto.ClienteDTO;
import com.banco.accountservice.dto.CuentaCreateRequest;
import com.banco.accountservice.dto.CuentaResponse;
import com.banco.accountservice.entity.Cuenta;
import com.banco.accountservice.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService{
    private final CuentaRepository cuentaRepository;
    private final CustomerClient customerClient;
    private final Random random = new Random();

    @Override
    @Transactional
    public CuentaResponse crearCuenta(CuentaCreateRequest request) {

        try {
            ClienteDTO cliente = customerClient.obtenerClientePorId(request.clienteId());

            if (cliente == null || !cliente.activo()) {
                throw new RuntimeException("El cliente se encuentra inactivo y no puede abrir cuentas.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al validar el cliente: El ID " + request.clienteId() + " no existe en el sistema.");
        }

        String numeroCuenta = generarNumeroCuentaUnico();
        String cci = generarCciUnico(numeroCuenta);

        Cuenta cuenta = Cuenta.builder()
                .numeroCuenta(numeroCuenta)
                .cci(cci)
                .saldo(BigDecimal.ZERO)
                .tipoCuenta(request.tipoCuenta().toUpperCase())
                .clienteId(request.clienteId())
                .activo(true)
                .build();

        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return mapToResponse(cuentaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponse> listarCuentas() {
        return cuentaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponse obtenerPorId(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + id));
        return mapToResponse(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponse> obtenerCuentaUsuarioLogueado(Principal principal) {

        String dniUsuarioLogueado = principal.getName();

        ClienteDTO cliente = customerClient.obtenerClientePorDni(dniUsuarioLogueado);

        Long clienteIdReal = cliente.id();
        return cuentaRepository.findByClienteId(clienteIdReal).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CuentaResponse> obtenerCuentasPorDniCliente(String dni) {

        ClienteDTO cliente = customerClient.obtenerClientePorDni(dni);

        if (cliente == null) {
            throw new RuntimeException("No existe un cliente registrado con el DNI: " + dni);
        }

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(cliente.id());
        if (cuentas.isEmpty()) {
            throw new RuntimeException("El cliente no tiene cuentas bancarias asignadas.");
        }

        return cuentas.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponse obtenerPorNumeroCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("La cuenta número " + numeroCuenta + " no existe."));
        return mapToResponse(cuenta);
    }

    @Override
    @Transactional
    public CuentaResponse cambiarEstadoCuenta(Long id, boolean activo) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + id));

        cuenta.setActivo(activo);
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return mapToResponse(cuentaActualizada);
    }

    @Override
    @Transactional
    public CuentaResponse actualizarSaldo(Long id, BigDecimal nuevoSaldo) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + id));

        if (!cuenta.getActivo()) {
            throw new RuntimeException("No se puede modificar el saldo de una cuenta bloqueada o inactiva.");
        }

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Fondos insuficientes: El saldo no puede ser menor a cero.");
        }

        cuenta.setSaldo(nuevoSaldo);
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return mapToResponse(cuentaActualizada);
    }


    //metodos auxiliares de generacion cuenta
    private String generarNumeroCuentaUnico() {
        String numero;
        do {
            //un número de cuenta de 12 digitos
            long primerTramo = 100000 + random.nextInt(900000);
            long segundoTramo = 100000 + random.nextInt(900000);
            numero = String.valueOf(primerTramo) + segundoTramo;
        } while (cuentaRepository.existsByNumeroCuenta(numero));
        return numero;
    }

    private String generarCciUnico(String numeroCuenta) {
        String cci;
        do {
            //simulamos un CCI de 20 digitos
            long control = 10000 + random.nextInt(90000);
            cci = "011" + numeroCuenta + control;
        } while (cuentaRepository.existsByCci(cci));
        return cci;
    }

    private CuentaResponse mapToResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getCci(),
                cuenta.getSaldo(),
                cuenta.getTipoCuenta(),
                cuenta.getClienteId(),
                cuenta.getActivo(),
                cuenta.getFechaCreacion()
        );
    }
}
