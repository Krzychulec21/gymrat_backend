package com.example.gymrat.service;

import com.example.gymrat.DTO.trainingPlan.CreateTrainingPlanDTO;
import com.example.gymrat.DTO.trainingPlan.ExerciseInPlanDTO;
import com.example.gymrat.DTO.trainingPlan.TrainingPlanResponseDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.mapper.TrainingPlanMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final UserService userService;
    private final TrainingPlanMapper trainingPlanMapper;
    private final ExerciseRepository exerciseRepository;

    public void saveTrainingPlan(CreateTrainingPlanDTO dto) {
        User currentUser = userService.getCurrentUser();

        List<Long> exerciseIds = dto.exercises().stream()
                .map(ExerciseInPlanDTO::exerciseId)
                .toList();

        List<Exercise> exercises = exerciseRepository.findAllById(exerciseIds);

        //we check here if all passed exercises id exist in database
        if (exercises.size() != exerciseIds.size()) {
            throw new ResourceNotFoundException("One or more exercises not found");
        }
        TrainingPlan trainingPlan = trainingPlanMapper.toEntity(dto, exercises);
        trainingPlan.setAuthor(currentUser);
        trainingPlan.setDifficultyLevel(calculateDifficultyOfTrainingPlan(trainingPlan));

    }

    public int calculateDifficultyOfTrainingPlan(TrainingPlan trainingPlan) {
        return Math.round(
                (float) trainingPlan.getExercisesInPlan()
                        .stream()
                        .map(exerciseInPlan -> exerciseInPlan.getExercise().getExerciseInfo().getDifficultyLevel())
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0)
        );
    }

}
