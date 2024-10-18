package com.example.gymrat.DTO.workout;

import java.util.List;

public record ExerciseSessionDTO(
        Long exerciseId,
        List<ExerciseSetDTO> sets
) {
}
