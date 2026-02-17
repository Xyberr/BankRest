package com.example.bankcards.mapper;

import com.example.bankcards.dto.user.UserRequestDTO;
import com.example.bankcards.dto.user.UserResponseDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // кодируется в сервисе
        user.setRole(Role.USER);
        return user;
    }

    public UserResponseDTO toDto(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getPhoneNumber(),
                user.getRole().name()
        );
    }
}
