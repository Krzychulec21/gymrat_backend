package com.example.gymrat.DTO.challenge;

public record UserRankingDTO(
        Long userId,
        String nickname,
        int goldMedals,
        int silverMedals,
        int bronzeMedals,
        int totalPoints,
        int rankPosition
) {
}