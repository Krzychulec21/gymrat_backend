package com.example.gymrat.DTO.trainingPlan;

public record ExerciseInPlanResponseDTO(
        Long id,
        String exerciseName,
        String customInstructions
) {
}
