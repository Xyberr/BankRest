package com.example.bankcards.service.authService;

import com.example.bankcards.dto.auth.AuthRequest;
import com.example.bankcards.dto.auth.AuthResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user.getPhoneNumber());
        String refreshToken = jwtService.generateRefreshToken(user.getPhoneNumber());

        return new AuthResponse(
                user.getId(),
                user.getPhoneNumber(),
                user.getRole().name(),
                accessToken,
                refreshToken
        );
    }

    @Override
    public AuthResponse register(AuthRequest request) {

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BadRequestException("Phone number already in use");
        }

        User user = User.builder()
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(saved.getPhoneNumber());
        String refreshToken = jwtService.generateRefreshToken(saved.getPhoneNumber());

        return new AuthResponse(
                saved.getId(),
                saved.getPhoneNumber(),
                saved.getRole().name(),
                accessToken,
                refreshToken
        );
    }
}