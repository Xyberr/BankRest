package com.example.bankcards.service.userService;

import com.example.bankcards.dto.user.UserRequestDTO;
import com.example.bankcards.dto.user.UserResponseDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final UserValidator validator;

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {

        validator.validate(dto);

        User saved = repository.save(mapper.toEntity(dto));

        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return mapper.toDto(findUser(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    public void deleteUser(Long id) {
        repository.delete(findUser(id));
    }

    private User findUser(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("User not found: " + id));
    }
}