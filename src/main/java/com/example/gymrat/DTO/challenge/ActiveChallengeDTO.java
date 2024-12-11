package com.example.gymrat.DTO.challenge;

public record ActiveChallengeDTO(
        Long id,
        String challengeName,
        long daysLeft,
        int participantCount,
        boolean isPublic
) {
}