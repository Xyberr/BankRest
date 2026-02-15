package com.example.bankcards.util;


import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtil {

    private static final int SCALE = 2;

    private MoneyUtil() {
    }

    public static BigDecimal normalize(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
        }
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }
}