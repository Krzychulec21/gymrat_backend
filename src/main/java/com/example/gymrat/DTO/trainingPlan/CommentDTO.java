package com.example.gymrat.DTO.trainingPlan;

import jakarta.validation.constraints.NotBlank;

public record CommentDTO(
        @NotBlank String content
) {
}
