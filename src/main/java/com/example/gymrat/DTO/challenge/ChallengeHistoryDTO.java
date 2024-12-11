package com.example.gymrat.DTO.challenge;

import java.time.LocalDate;

public record ChallengeHistoryDTO(
        Long id,
        String name,
        String typeName,
        String statusName,
        LocalDate startDate,
        LocalDate endDate,
        boolean isPublic,
        int numberOfParticipants,
        String exerciseName,
        boolean userHasScore
) {
}
