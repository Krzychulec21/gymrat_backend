package com.example.gymrat.DTO.trainingPlan;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UpdateTrainingPlanDTO(
        @NotBlank String name,
        String description,
        @NotEmpty List<@Valid ExerciseInPlanDTO> exercises
) {
}
