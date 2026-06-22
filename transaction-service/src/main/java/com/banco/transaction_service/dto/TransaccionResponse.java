package com.banco.transaction_service.dto;

import com.banco.transaction_service.enums.EstadoTransaccion;
import com.banco.transaction_service.enums.TipoTransaccion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransaccionResponse(
        Long id,
        String cuentaOrigen,
        String cuentaDestino,
        BigDecimal monto,
        TipoTransaccion tipo,
        EstadoTransaccion estado,
        LocalDateTime fechaTransaccion
) {}
