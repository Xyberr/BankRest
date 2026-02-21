package com.example.bankcards.dto.refresh;

public record RefreshRequest(
        String refreshToken,
        String deviceId,
        String fingerprint
) {}