package com.example.gymrat.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "Data Transfer Object for email")
public record EmailDTO(
        @Email(message = "Email should be valid")
        @Schema(description = "Email address", example = "user@example.com")
        String email
) {}
