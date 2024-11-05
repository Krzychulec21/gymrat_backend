package com.example.gymrat.DTO.trainingPlan;
import jakarta.validation.constraints.NotNull;

public record ExerciseInPlanDTO (
        @NotNull Long exerciseId,
        String customInstructions
) {
}
