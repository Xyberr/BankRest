package com.example.bankcards.dto.refresh;

public record RefreshResponse(
        String accessToken,
        String refreshToken
) {}