package com.example.bankcards.validator;

import com.example.bankcards.dto.user.UserRequestDTO;
import com.example.bankcards.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validate(UserRequestDTO dto) {
        if (dto.getPassword().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters");
        }
        // можно добавить кастомную проверку уникальности email и username
    }
}