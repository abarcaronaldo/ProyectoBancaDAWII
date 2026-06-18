package com.banco.dto;

public record LoginResponse(
		String token,
		String tokenType,
		Long expiresInMinutes
) {
}
