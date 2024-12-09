package com.example.gymrat.DTO.post;

import java.time.LocalDate;

public record PostResponseDTO(
        Long id,
        String description,
        String imageUrl,
        LocalDate timestamp,
        Long workoutSessionId,
        int reactionCount,
        boolean reactedByCurrentUser
) {
}
