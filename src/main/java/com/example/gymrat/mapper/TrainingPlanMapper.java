package com.example.gymrat.mapper;

import com.example.gymrat.DTO.trainingPlan.*;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.model.CategoryName;
import com.example.gymrat.model.Exercise;
import com.example.gymrat.model.ExerciseInPlan;
import com.example.gymrat.model.TrainingPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class TrainingPlanMapper {

    public TrainingPlan toEntity(CreateTrainingPlanDTO dto, List<Exercise> exercises) {
        TrainingPlan trainingPlan = new TrainingPlan();
        trainingPlan.setName(dto.name());
        trainingPlan.setDescription(dto.description());

        Set<CategoryName> categories = new HashSet<>();

        List<ExerciseInPlan> exercisesInPlan = dto.exercises().stream().map(exerciseInPlanDTO -> {
            ExerciseInPlan exerciseInPlan = new ExerciseInPlan();

            Exercise exercise = exercises.stream()
                    .filter(e -> e.getId().equals(exerciseInPlanDTO.exerciseId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

            categories.add(exercise.getCategory());
            exerciseInPlan.setExercise(exercise);
            exerciseInPlan.setCustomInstructions(exerciseInPlanDTO.customInstructions());
            exerciseInPlan.setTrainingPlan(trainingPlan);

            return exerciseInPlan;
        }).toList();

        trainingPlan.setCategories(categories);
        trainingPlan.setExercisesInPlan(exercisesInPlan);

        return trainingPlan;
    }

    public TrainingPlanResponseDTO mapToResponseDTO(TrainingPlan trainingPlan, int likeCount, List<CommentResponseDTO> comments, String userReaction) {
        List<ExerciseInPlanResponseDTO> exercises = trainingPlan.getExercisesInPlan().stream().map(exerciseInPlan ->
                new ExerciseInPlanResponseDTO(
                        exerciseInPlan.getExercise().getId(),
                        exerciseInPlan.getExercise().getName(),
                        exerciseInPlan.getCustomInstructions()
                )).toList();

        return new TrainingPlanResponseDTO(
                trainingPlan.getId(),
                trainingPlan.getName(),
                trainingPlan.getDescription(),
                trainingPlan.getAuthor().getId(),
                trainingPlan.getAuthor().getNickname(),
                trainingPlan.getDifficultyLevel(),
                trainingPlan.getCategories(),
                exercises,
                comments,
                likeCount,
                userReaction
        );
    }

    public TrainingPlanSummaryDTO mapToSummaryDTO(TrainingPlan trainingPlan, boolean isFavorite) {
        return new TrainingPlanSummaryDTO(
                trainingPlan.getId(),
                trainingPlan.getName(),
                trainingPlan.getAuthor().getNickname(),
                trainingPlan.getCategories(),
                trainingPlan.getDifficultyLevel(),
                trainingPlan.getLikeCount(),
                isFavorite
        );
    }

}
