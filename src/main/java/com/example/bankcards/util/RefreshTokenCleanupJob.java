package com.example.bankcards.util;

import com.example.bankcards.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupJob {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 * * * *") // каждый час
    public void cleanup() {
        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}