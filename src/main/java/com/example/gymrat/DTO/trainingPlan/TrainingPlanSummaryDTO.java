package com.example.gymrat.DTO.trainingPlan;

import com.example.gymrat.model.CategoryName;

import java.util.Set;

//DTO to display general info in table (all info are send in TrainingPlanResponseDTO)
public record TrainingPlanSummaryDTO(
        Long id,
        String name,
        String authorNickname,
        Set<CategoryName> categories,
        Integer difficultyLevel,
        Integer likeCount
) {
}
