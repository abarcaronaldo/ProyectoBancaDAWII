package com.banco.dto;

public record UserResponse(
		Long id,
		String username,
		String email
) {
}
