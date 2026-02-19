package com.example.bankcards.controller.user;

import com.example.bankcards.dto.user.UserResponseDTO;
import com.example.bankcards.service.userService.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final IUserService userService;

    @GetMapping("/{id}")
    public UserResponseDTO get(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}