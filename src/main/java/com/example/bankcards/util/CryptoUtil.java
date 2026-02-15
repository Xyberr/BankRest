package com.example.bankcards.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class CryptoUtil {

    @Value("${crypto.secret}")
    private String secret;

    private SecretKeySpec secretKeySpec;

    @PostConstruct
    public void init() {
        byte[] key = secret.getBytes(StandardCharsets.UTF_8);
        secretKeySpec = new SecretKeySpec(key, "AES");
    }

    public String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new RuntimeException("Encryption failed", ex);
        }
    }

    public String decrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decoded = Base64.getDecoder().decode(value);
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException("Decryption failed", ex);
        }
    }
}