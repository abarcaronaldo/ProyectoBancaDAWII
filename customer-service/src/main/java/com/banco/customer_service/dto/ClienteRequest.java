package com.banco.customer_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "El DNI es obligatorio")
        @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 dígitos")
        @Pattern(regexp = "\\d+", message = "El DNI debe contener solo números")
        String dni,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
        String apellido,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El formato del correo no es válido")
        String email,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(min = 9, max = 9, message = "El teléfono debe tener exactamente 9 dígitos")
        @Pattern(regexp = "9\\d+", message = "El teléfono debe empezar con 9")
        String telefono,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 150, message = "La dirección no puede exceder los 150 caracteres")
        String direccion
) {
}
