package com.example.gymrat.DTO.challenge;

import java.time.LocalDate;

public record ChallengeResponseDTO(
        Long id,
        String name,
        String typeName,
        String statusName,
        LocalDate startDate,
        LocalDate endDate,
        boolean isPublic,
        int numberOfParticipants
) {
}