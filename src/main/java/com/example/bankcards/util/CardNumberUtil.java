package com.example.bankcards.util;

import java.util.concurrent.ThreadLocalRandom;

public final class CardNumberUtil {

    private CardNumberUtil() {}

    public static String generate() {
        String bin = "400000"; // Visa test BIN
        StringBuilder number = new StringBuilder(bin);

        while (number.length() < 15) {
            number.append(ThreadLocalRandom.current().nextInt(10));
        }

        int check = luhnDigit(number.toString());
        number.append(check);

        return number.toString();
    }

    private static int luhnDigit(String number) {
        int sum = 0;
        boolean alternate = true;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = number.charAt(i) - '0';

            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }

            sum += n;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }
}