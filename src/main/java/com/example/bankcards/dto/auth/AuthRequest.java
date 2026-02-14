package com.example.bankcards.dto.auth;

public record AuthRequest(
        String email,
        String password
) {}