package com.banco.customer_service.dto;

public record UserResponse(
        Long id,
        String username,
        String email,
        boolean activo,
        String tokenActivacion,
        String rol
) {}
