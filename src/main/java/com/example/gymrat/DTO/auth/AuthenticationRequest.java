package com.example.gymrat.DTO.auth;

public record AuthenticationRequest(
        String email,

        String password
) {
}
