package com.example.bankcards.dto.transfer;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransferRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long fromCardId;

    @NotNull
    private Long toCardId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    public Long getUserId() {
        return userId;
    }

    public Long getFromCardId() {
        return fromCardId;
    }

    public Long getToCardId() {
        return toCardId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}