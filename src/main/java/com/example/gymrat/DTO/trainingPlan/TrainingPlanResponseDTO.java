package com.example.gymrat.DTO.trainingPlan;

import com.example.gymrat.model.CategoryName;

import java.util.List;
import java.util.Set;

public record TrainingPlanResponseDTO(
        Long id,
        String name,
        String description,
        String authorNickname,
        Integer difficultyLevel,
        Set<CategoryName> categories,
        List<ExerciseInPlanResponseDTO> exercises,
        List<CommentResponseDTO> comments,
        Integer likeCount
) {}
