package com.example.gymrat.DTO.challenge;

import java.time.LocalDateTime;

public record ChallengeParticipantResponseDTO(
        Long id,
        String username,
        Double score,
        LocalDateTime lastUpdated
) {
}
