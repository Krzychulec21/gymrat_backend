package com.example.gymrat.DTO.challenge;

import java.time.LocalDate;

public record ChallengeRequestDTO(
        String name,
        Long typeId,
        LocalDate endDate,
        boolean isPublic,
        Long exerciseId
) {
}
