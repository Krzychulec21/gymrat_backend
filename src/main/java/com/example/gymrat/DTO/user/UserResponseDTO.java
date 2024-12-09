package com.example.gymrat.DTO.user;

public record UserResponseDTO(
        Long id,
        String firstName,

        String lastName,
        String nickname,

        String email
) {
}
