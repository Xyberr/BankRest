package com.example.bankcards.util;

public final class MaskUtil {

    private MaskUtil() {}

    public static String maskCard(String number) {
        if (number == null || number.length() < 4)
            return "****";

        return "**** **** **** " + number.substring(number.length() - 4);
    }
}