package com.example.gymrat.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object representing a user's basic information")
public record UserResponseDTO(
        @Schema(description = "Unique identifier of the user", example = "1")
        Long id,

        @Schema(description = "First name of the user", example = "John")
        String firstName,

        @Schema(description = "Last name of the user", example = "Doe")
        String lastName,

        @Schema(description = "Email address of the user", example = "john.doe@example.com")
        String email
) {}
