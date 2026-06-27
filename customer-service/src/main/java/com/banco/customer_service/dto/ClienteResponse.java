package com.banco.customer_service.dto;

import java.time.LocalDateTime;


public record ClienteResponse(
        Long id,
        String dni,
        String nombre,
        String apellido,
        String email,
        String telefono,
        String direccion,
        LocalDateTime creadoEn,
        Boolean activo,
        String tokenActivacionDigital
) {
}
