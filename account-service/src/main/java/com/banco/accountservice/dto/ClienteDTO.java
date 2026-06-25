package com.banco.accountservice.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ClienteDTO(
        Long id,
        String dni,
        String nombre,
        String apellido,
        String email,
        String telefono,
        String direccion,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        LocalDateTime creadoEn,
        Boolean activo
) {}