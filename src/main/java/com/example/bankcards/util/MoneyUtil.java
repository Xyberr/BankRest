package com.example.bankcards.util;

import java.math.BigDecimal;
import java.math.BigDecimal;

public final class MoneyUtil {

    private static final int SCALE = 2;

    private MoneyUtil() {}

    public static BigDecimal normalize(BigDecimal value) {
        return value.setScale(SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return normalize(a.add(b));
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return normalize(a.subtract(b));
    }

    public static boolean isNegative(BigDecimal v) {
        return v.compareTo(BigDecimal.ZERO) < 0;
    }
}