package com.example.bankcards.controller.auth;

import com.example.bankcards.dto.auth.AuthRequest;
import com.example.bankcards.dto.auth.AuthResponse;
import com.example.bankcards.dto.logout.LogoutRequest;
import com.example.bankcards.dto.refresh.RefreshRequest;
import com.example.bankcards.dto.refresh.RefreshResponse;
import com.example.bankcards.service.authService.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.dto.auth.AuthRequest;
import com.example.bankcards.dto.auth.AuthResponse;
import com.example.bankcards.service.authService.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final HttpServletRequest httpRequest;

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody AuthRequest request,
            HttpServletResponse response
    ) {
        AuthResponse auth = authService.login(request, httpRequest);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", auth.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new AuthResponse(
                auth.userId(),
                auth.phoneNumber(),
                auth.role(),
                auth.accessToken(),
                null
        );
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request, httpRequest));
    }

    @PostMapping("/refresh")
    public RefreshResponse refresh(
            @CookieValue("refresh_token") String refreshToken,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        RefreshResponse refresh =
                authService.refresh(refreshToken, request);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refresh.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new RefreshResponse(refresh.accessToken(), null);
    }
    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest request) {
        authService.logout(request);
    }
}