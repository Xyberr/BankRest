package com.example.bankcards.dto.transfer;

import com.example.bankcards.entity.enums.TransferStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferResponse(
        Long id,
        Long fromCardId,
        Long toCardId,
        BigDecimal amount,
        TransferStatus status,
        Instant createdAt
) {
}