package com.example.gymrat.service;

import com.example.gymrat.DTO.trainingPlan.*;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.mapper.TrainingPlanMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final UserService userService;
    private final TrainingPlanMapper trainingPlanMapper;
    private final ExerciseRepository exerciseRepository;
    private final CommentService commentService;
    private final LikeService likeService;

    public void saveTrainingPlan(CreateTrainingPlanDTO dto) {
        User currentUser = userService.getCurrentUser();

        List<Long> exerciseIds = dto.exercises().stream()
                .map(ExerciseInPlanDTO::exerciseId)
                .toList();

        List<Exercise> exercises = exerciseRepository.findAllById(exerciseIds);

        // Validate exercises
        if (exercises.size() != exerciseIds.size()) {
            throw new ResourceNotFoundException("One or more exercises not found");
        }

        TrainingPlan trainingPlan = trainingPlanMapper.toEntity(dto, exercises);
        trainingPlan.setAuthor(currentUser);
        trainingPlan.setDifficultyLevel(calculateDifficultyOfTrainingPlan(trainingPlan));

        trainingPlanRepository.save(trainingPlan);
    }

    public TrainingPlanResponseDTO getTrainingPlanById(Long id) {
        TrainingPlan trainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

        int likeCount = likeService.getLikeCount(id);
        List<CommentResponseDTO> comments = commentService.getComments(id, 0, Integer.MAX_VALUE, "dateCreated", "asc").getContent();

        return trainingPlanMapper.mapToResponseDTO(trainingPlan, likeCount, comments);
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

    public Page<TrainingPlanSummaryDTO> getAllTrainingPlans(int page, int size, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TrainingPlan> trainingPlansPage = trainingPlanRepository.findAll(pageable);

        return trainingPlansPage.map(trainingPlanMapper::mapToSummaryDTO);
    }

    public Page<TrainingPlanSummaryDTO> getTrainingPlansByUser(Long userId, int page, int size, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TrainingPlan> trainingPlansPage = trainingPlanRepository.findByAuthorId(userId, pageable);

        return trainingPlansPage.map(trainingPlanMapper::mapToSummaryDTO);
    }

    public void updateTrainingPlan(Long planId, UpdateTrainingPlanDTO dto) {
        User currentUser = userService.getCurrentUser();

        TrainingPlan trainingPlan = trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

        if (!trainingPlan.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only edit your own training plans");
        }

        List<Long> exerciseIds = dto.exercises().stream()
                .map(ExerciseInPlanDTO::exerciseId)
                .toList();

        List<Exercise> exercises = exerciseRepository.findAllById(exerciseIds);

        if (exercises.size() != exerciseIds.size()) {
            throw new ResourceNotFoundException("One or more exercises not found");
        }

        trainingPlan.setName(dto.name());
        trainingPlan.setDescription(dto.description());

        trainingPlan.getExercisesInPlan().clear();
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

        trainingPlan.getExercisesInPlan().addAll(exercisesInPlan);
        trainingPlan.setCategories(categories);

        trainingPlan.setDifficultyLevel(calculateDifficultyOfTrainingPlan(trainingPlan));

        trainingPlanRepository.save(trainingPlan);
    }

    public void deleteTrainingPlan(Long planId) {
        User currentUser = userService.getCurrentUser();

        TrainingPlan trainingPlan = trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

        if (!trainingPlan.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only delete your own training plans");
        }

        trainingPlanRepository.delete(trainingPlan);
    }
}
