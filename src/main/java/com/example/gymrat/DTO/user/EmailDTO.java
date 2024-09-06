package com.example.gymrat.DTO.user;

import jakarta.validation.constraints.Email;

public record EmailDTO(
        @Email(message = "Email should be valid")
        String email
) {}
