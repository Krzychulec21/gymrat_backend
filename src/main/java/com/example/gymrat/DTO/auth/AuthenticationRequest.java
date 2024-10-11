package com.example.gymrat.DTO.auth;

import io.swagger.v3.oas.annotations.media.Schema;


public record AuthenticationRequest(
        String email,

        String password
) {}
