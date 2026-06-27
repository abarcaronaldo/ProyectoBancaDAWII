package com.banco.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
		@NotBlank String username,
		@NotBlank @Email String email,
        @NotNull Long rolId
) {
}
