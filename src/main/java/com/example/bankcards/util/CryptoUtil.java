package com.example.bankcards.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class CryptoUtil {

    private static final String ALGO = "AES";

    private final SecretKeySpec keySpec;

    public CryptoUtil(@Value("${app.crypto.secret}") String secret) {
        byte[] key = secret.substring(0, 16).getBytes(StandardCharsets.UTF_8);
        this.keySpec = new SecretKeySpec(key, ALGO);
    }

    public String encrypt(String raw) {
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encrypted = cipher.doFinal(raw.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new IllegalStateException("Encryption failed", e);
        }
    }

    public String decrypt(String enc) {
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decoded = Base64.getDecoder().decode(enc);
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new IllegalStateException("Decryption failed", e);
        }
    }
}