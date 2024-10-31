package com.example.gymrat.DTO.trainingPlan;

import java.util.List;

public record CreateTrainingPlanDTO(
        String name,
        String description,
        List<ExerciseInPlanDTO> exercises
) {
}
