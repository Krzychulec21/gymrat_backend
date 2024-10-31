package com.example.gymrat.DTO.trainingPlan;

public record ExerciseInPlanDTO (
    Long exerciseId,
    String exerciseName,
    String customInstructions
) {
}
