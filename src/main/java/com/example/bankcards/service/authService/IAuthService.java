package com.example.bankcards.service.authService;

import com.example.bankcards.dto.auth.AuthRequest;
import com.example.bankcards.dto.auth.AuthResponse;

public interface IAuthService {

    AuthResponse login(AuthRequest request);

    AuthResponse register(AuthRequest request);
}