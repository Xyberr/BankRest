package com.example.bankcards.service.blacklistService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public void blacklist(String token, long expirationMillis) {
        redisTemplate.opsForValue()
                .set(token, "blacklisted",
                        Duration.ofMillis(expirationMillis));
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}