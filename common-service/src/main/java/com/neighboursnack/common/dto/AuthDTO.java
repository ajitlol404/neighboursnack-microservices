package com.neighboursnack.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthDTO {
    public record LoginRequestDTO(
            @NotNull(message = "Email is required")
            @NotBlank(message = "Email cannot be blank")
            @Email(message = "Email must be valid")
            @Size(max = 320, message = "Email must not exceed 320 characters")
            String email,

            @NotNull(message = "Password is required")
            @NotBlank(message = "Password cannot be blank")
            @Size(max = 100, message = "Password must not exceed 100 characters")
            String password
    ) {
    }
}