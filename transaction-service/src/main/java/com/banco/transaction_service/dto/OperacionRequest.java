package com.banco.transaction_service.dto;

import java.math.BigDecimal;

public record OperacionRequest(
        String cuenta,
        BigDecimal monto
) {}