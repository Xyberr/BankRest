package com.example.bankcards.service.authService;

import com.example.bankcards.dto.auth.AuthRequest;
import com.example.bankcards.dto.auth.AuthResponse;
import com.example.bankcards.dto.logout.LogoutRequest;
import com.example.bankcards.dto.refresh.RefreshRequest;
import com.example.bankcards.dto.refresh.RefreshResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {

    AuthResponse login(AuthRequest request, HttpServletRequest httpRequest);

    AuthResponse register(AuthRequest request, HttpServletRequest httpRequest);

    RefreshResponse refresh(String refreshToken, HttpServletRequest request);

    void logout(LogoutRequest request);
}