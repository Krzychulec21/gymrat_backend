package com.example.gymrat.DTO.workout;

import java.time.LocalDate;
import java.util.List;

public record WorkoutSessionResponseDTO(
        Long id,
        LocalDate date,
        String note,
        int numberOfExercises,
        String mainCategory,
        List<ExerciseSessionDTO> exerciseSessions
) {
}
