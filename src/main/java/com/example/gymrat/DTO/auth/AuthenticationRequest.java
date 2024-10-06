package com.example.gymrat.DTO.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for user authentication")
public record AuthenticationRequest(
        @Schema(description = "User's email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "User's password", example = "password123")
        String password
) {}
