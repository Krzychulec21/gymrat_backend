package com.example.gymrat.DTO.User.auth;

public record AuthenticationRequest (
        String email,
        String password
)
{}
