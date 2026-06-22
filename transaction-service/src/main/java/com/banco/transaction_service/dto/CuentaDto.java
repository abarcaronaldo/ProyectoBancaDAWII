package com.banco.transaction_service.dto;

import java.math.BigDecimal;

public record CuentaDto(
        Long id,
        String numeroCuenta,
        BigDecimal saldo,
        Boolean activo,
        Long clienteId
) {}
