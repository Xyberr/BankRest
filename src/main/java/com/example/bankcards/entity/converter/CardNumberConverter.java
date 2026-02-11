package com.example.bankcards.entity.converter;

import com.example.bankcards.util.CryptoUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Converter
@Component
@RequiredArgsConstructor
public class CardNumberConverter implements AttributeConverter<String, String> {

    private final CryptoUtil crypto;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : crypto.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : crypto.decrypt(dbData);
    }
}