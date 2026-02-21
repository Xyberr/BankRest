package com.example.bankcards.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        String phoneNumber,
        String password,
        String deviceId,
        String fingerprint,
        String email
) {
    @Override
    public String phoneNumber() {
        return phoneNumber;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public String deviceId() {
        return deviceId;
    }

    @Override
    public String fingerprint() {
        return fingerprint;
    }

    @Override
    public String email() {
        return email;
    }
}