package com.example.demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequestDto(
        @NotBlank
        @Email(message = "Invalid email")
        String email,
        @NotBlank
        String password
) {
}
