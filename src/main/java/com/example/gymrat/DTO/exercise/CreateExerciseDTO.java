package com.example.gymrat.DTO.exercise;

import com.example.gymrat.model.CategoryName;

public record CreateExerciseDTO (
        String name,
        CategoryName categoryName
){}
