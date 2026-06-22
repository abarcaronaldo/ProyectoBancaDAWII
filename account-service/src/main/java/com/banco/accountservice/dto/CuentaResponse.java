package com.banco.accountservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CuentaResponse(
        Long id,
        String numeroCuenta,
        String cci,
        BigDecimal saldo,
        String tipoCuenta,
        Long clienteId,
        Boolean activo,
        LocalDateTime fechaCreacion
) {}
