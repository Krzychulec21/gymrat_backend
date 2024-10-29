package com.example.gymrat.DTO.exercise;

import java.util.List;

public record ExerciseInfoResponseDTO (
        List<String> description,
        String videoId
) {}
