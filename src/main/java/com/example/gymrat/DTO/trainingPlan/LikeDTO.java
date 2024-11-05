package com.example.gymrat.DTO.trainingPlan;

import jakarta.validation.constraints.NotNull;

public record LikeDTO(
        @NotNull Boolean isLike // true-like false-dislike
) {
}
