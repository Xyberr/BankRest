package com.example.bankcards.util;


public final class MaskUtil {

    private MaskUtil() {
    }

    public static String maskCard(String number) {
        if (number == null || number.length() < 4) {
            return "****";
        }

        int visibleDigits = 4;
        int maskedLength = number.length() - visibleDigits;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < maskedLength; i++) {
            sb.append('*');
            if ((i + 1) % 4 == 0) {
                sb.append(' ');
            }
        }

        sb.append(number.substring(maskedLength));

        return sb.toString().trim();
    }
}