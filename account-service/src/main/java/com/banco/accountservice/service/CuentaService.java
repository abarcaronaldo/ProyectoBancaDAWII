package com.banco.accountservice.service;

import com.banco.accountservice.dto.CuentaCreateRequest;
import com.banco.accountservice.dto.CuentaResponse;

import java.security.Principal;
import java.util.List;

public interface CuentaService {
    CuentaResponse crearCuenta(CuentaCreateRequest request);
    List<CuentaResponse> listarCuentas();
    CuentaResponse obtenerPorId(Long id);
    List<CuentaResponse> obtenerCuentaUsuarioLogueado(Principal principal);
    List<CuentaResponse> obtenerCuentasPorDniCliente(String dni);
    CuentaResponse obtenerPorNumeroCuenta(String numeroCuenta);

    //extra
    CuentaResponse cambiarEstadoCuenta(Long id, boolean activo);
    CuentaResponse actualizarSaldo(Long id, java.math.BigDecimal nuevoSaldo);
}
