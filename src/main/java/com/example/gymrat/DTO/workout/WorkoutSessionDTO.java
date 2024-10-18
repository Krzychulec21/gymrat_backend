package com.example.gymrat.DTO.workout;

import java.time.LocalDate;
import java.util.List;

public record WorkoutSessionDTO(
        LocalDate date,
        String note,
        List<ExerciseSessionDTO> exerciseSessions
) {
}
