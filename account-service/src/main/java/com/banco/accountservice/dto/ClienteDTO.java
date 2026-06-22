package com.banco.accountservice.dto;

public record ClienteDTO(
        Long id,
        String nombre,
        String apellido,
        String dni,
        Boolean activo
) {}