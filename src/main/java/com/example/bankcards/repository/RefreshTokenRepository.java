package com.example.bankcards.repository;

import com.example.bankcards.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserIdAndDeviceId(Long userId, String deviceId);

    void deleteByExpiryDateBefore(Instant now);
}