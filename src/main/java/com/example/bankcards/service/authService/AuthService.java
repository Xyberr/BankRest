package com.example.bankcards.service.authService;

import com.example.bankcards.dto.auth.AuthRequest;
import com.example.bankcards.dto.auth.AuthResponse;
import com.example.bankcards.dto.logout.LogoutRequest;
import com.example.bankcards.dto.refresh.RefreshResponse;
import com.example.bankcards.entity.LoginAudit;
import com.example.bankcards.entity.RefreshToken;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.repository.LoginAuditRepository;
import com.example.bankcards.repository.RefreshTokenRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtSecurity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final JwtSecurity jwtSecurity;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginAuditRepository loginAuditRepository;


    @Override
    public AuthResponse login(AuthRequest request, HttpServletRequest httpRequest) {

        User user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {

            loginAuditRepository.save(
                    LoginAudit.builder()
                            .userId(user.getId())
                            .ip(httpRequest.getRemoteAddr())
                            .deviceId(request.deviceId())
                            .success(false)
                            .timestamp(Instant.now())
                            .build()
            );

            throw new BadRequestException("Invalid credentials");
        }

        String ip = httpRequest.getRemoteAddr();

        String accessToken =
                jwtSecurity.generateAccessToken(user.getPhoneNumber());

        RefreshToken refreshToken =
                createOrReplaceRefreshToken(
                        user,
                        request.deviceId(),
                        request.fingerprint(),
                        ip
                );

        loginAuditRepository.save(
                LoginAudit.builder()
                        .userId(user.getId())
                        .ip(ip)
                        .deviceId(request.deviceId())
                        .success(true)
                        .timestamp(Instant.now())
                        .build()
        );

        return new AuthResponse(
                user.getId(),
                user.getPhoneNumber(),
                user.getRole().name(),
                accessToken,
                refreshToken.getToken()
        );
    }

    @Override
    public AuthResponse register(AuthRequest request, HttpServletRequest httpRequest) {

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new BadRequestException("Phone number already in use");
        }

        User user = User.builder()
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);

        String accessToken =
                jwtSecurity.generateAccessToken(saved.getPhoneNumber());

        RefreshToken refreshToken =
                createOrReplaceRefreshToken(
                        saved,
                        request.deviceId(),
                        request.fingerprint(),
                        httpRequest.getRemoteAddr()
                );

        return new AuthResponse(
                saved.getId(),
                saved.getPhoneNumber(),
                saved.getRole().name(),
                accessToken,
                refreshToken.getToken()
        );
    }

    @Override
    public RefreshResponse refresh(String refreshTokenValue,
                                   HttpServletRequest request) {

        String currentIp = request.getRemoteAddr();

        RefreshToken stored = refreshTokenRepository
                .findByToken(refreshTokenValue)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        if (stored.isRevoked())
            throw new BadRequestException("Token revoked");

        if (stored.getExpiryDate().isBefore(Instant.now()))
            throw new BadRequestException("Token expired");

        if (!stored.getIpAddress().equals(currentIp))
            throw new BadRequestException("IP mismatch");

        User user = stored.getUser();

        refreshTokenRepository.delete(stored);

        String newAccess =
                jwtSecurity.generateAccessToken(user.getPhoneNumber());

        RefreshToken newRefresh =
                createOrReplaceRefreshToken(
                        user,
                        stored.getDeviceId(),
                        stored.getFingerprint(),
                        currentIp
                );

        return new RefreshResponse(
                newAccess,
                newRefresh.getToken()
        );
    }

    @Override
    public void logout(LogoutRequest request) {

        refreshTokenRepository.findByToken(request.refreshToken())
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    private RefreshToken createOrReplaceRefreshToken(
            User user,
            String deviceId,
            String fingerprint,
            String ipAddress
    ) {

        refreshTokenRepository
                .findByUserIdAndDeviceId(user.getId(), deviceId)
                .ifPresent(refreshTokenRepository::delete);

        String refreshTokenValue =
                jwtSecurity.generateRefreshToken(user.getPhoneNumber());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .expiryDate(jwtSecurity.getRefreshExpirationInstant())
                .revoked(false)
                .deviceId(deviceId)
                .fingerprint(fingerprint)
                .ipAddress(ipAddress)
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
}