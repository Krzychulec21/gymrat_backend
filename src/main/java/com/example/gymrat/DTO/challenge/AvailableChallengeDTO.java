package com.example.gymrat.DTO.challenge;

public record AvailableChallengeDTO(
        Long id,
        String authorNickname,
        String challengeTypeName,
        String exerciseName, // if type PASS then null
        long daysLeft,
        int participantCount,
        boolean isPublic
) {
}