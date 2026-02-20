package com.example.bankcards.dto.auth;

public record AuthResponse(
        Long userId,
        String phoneNumber,
        String role,
        String accessToken,
        String refreshToken
) {}