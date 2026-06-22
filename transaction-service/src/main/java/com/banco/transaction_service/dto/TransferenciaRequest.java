package com.banco.transaction_service.dto;

import java.math.BigDecimal;

public record TransferenciaRequest(
        String cuentaOrigen,
        String cuentaDestino,
        BigDecimal monto
) {}
