package com.banco.dto;

public record ActivarCuentaRequest(
        String token,
        String nuevaPassword
) {}
