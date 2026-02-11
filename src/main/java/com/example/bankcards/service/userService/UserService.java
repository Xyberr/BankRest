package com.example.bankcards.service.userService;

import com.example.bankcards.dto.user.UserRequestDTO;
import com.example.bankcards.dto.user.UserResponseDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final UserValidator validator;

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        validator.validate(dto); // отдельный слой валидации
        User user = mapper.toEntity(dto);
        userRepository.save(user);
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }
}