package com.example.bankcards.mapper;

import com.example.bankcards.dto.user.UserRequestDTO;
import com.example.bankcards.dto.user.UserResponseDTO;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toDto(User user) {
        if (user == null) return null;
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getPassword());
    }

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(dto.getPassword());
        return user;
    }
}