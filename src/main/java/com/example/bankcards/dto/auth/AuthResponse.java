package com.example.bankcards.dto.auth;

public record AuthResponse(
        Long id,
        String email,
        String role
) {}