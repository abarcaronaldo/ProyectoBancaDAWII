package com.banco.accountservice.dto;

public record CuentaCreateRequest(
        Long clienteId,
        String tipoCuenta
) {}
