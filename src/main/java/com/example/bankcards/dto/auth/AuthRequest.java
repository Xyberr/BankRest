package com.example.bankcards.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class AuthRequest {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    public String getEmail() {return email;}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}