package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.user.UserRequestDTO;
import com.example.bankcards.dto.user.UserResponseDTO;
import com.example.bankcards.service.userService.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final IUserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@Valid @RequestBody UserRequestDTO dto) {
        return userService.createUser(dto);
    }

    @GetMapping("/{id}")
    public UserResponseDTO get(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public Page<UserResponseDTO> getAll(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}