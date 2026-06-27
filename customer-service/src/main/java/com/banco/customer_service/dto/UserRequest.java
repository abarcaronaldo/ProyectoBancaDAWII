package com.banco.customer_service.dto;

public record UserRequest(
        String username,
        String email,
        Long rolId
) {
}
