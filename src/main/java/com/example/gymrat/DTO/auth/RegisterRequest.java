package com.example.gymrat.DTO.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for user registration")
public record RegisterRequest(
        @NotBlank(message = "First name is required")
        @Schema(description = "First name of the user", example = "John")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Schema(description = "Last name of the user", example = "Doe")
        String lastName,

        @NotBlank(message = "Username is required")
        @Schema(description = "Nickname of the user", example = "johndoe")
        String nickname,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required")
        @Schema(description = "Email address of the user", example = "john.doe@example.com")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password should be at least 8 characters long")
        @Schema(description = "Password for the account", example = "password123")
        String password
) {}
