package com.example.gymrat.DTO.workout;

import java.util.List;

public record ExerciseSessionDTO(
        Long exerciseId,
        String exerciseName,
        List<ExerciseSetDTO> sets
) {
}
