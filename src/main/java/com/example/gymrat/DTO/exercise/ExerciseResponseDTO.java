package com.example.gymrat.DTO.exercise;

import com.example.gymrat.model.CategoryName;

public record ExerciseResponseDTO (
        Long id,
        String name,
        CategoryName categoryName

){}
