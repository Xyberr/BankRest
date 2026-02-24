package com.example.bankcards.dto.transfer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public record TransferRequest(

        @NotNull
        Long fromCardId,

        @NotNull
        Long toCardId,

        @NotNull
        @Positive
        BigDecimal amount
) {
}