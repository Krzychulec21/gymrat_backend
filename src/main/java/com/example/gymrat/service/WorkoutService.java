package com.example.gymrat.service;

import com.example.gymrat.DTO.workout.ExerciseSessionDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionResponseDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.mapper.WorkoutMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.ExerciseSessionRepository;
import com.example.gymrat.repository.ExerciseSetRepository;
import com.example.gymrat.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseService exerciseService;

    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseSetRepository exerciseSetRepository;
    private final ExerciseSessionRepository exerciseSessionRepository;
    private final UserService userService;
    private final WorkoutMapper workoutMapper;


    public Long saveWorkout(WorkoutSessionDTO workoutSessionDTO) {
        WorkoutSession workoutSession = workoutMapper.mapToEntity(workoutSessionDTO);
        User user = userService.getCurrentUser();
        workoutSession.setUser(user);
        workoutSessionRepository.save(workoutSession);
        return workoutSession.getId();
    }


    public Integer getNumberOfUserWorkouts() {
        User user = userService.getCurrentUser();
        int count = workoutSessionRepository.countAllByUserId(user.getId());
        System.out.println("liczba treningow" + count);
        return count;
    }

    public Double getTotalWeightLiftedByUser() {
        User user = userService.getCurrentUser();
        return workoutSessionRepository.findTotalWeightLiftedByUser(user.getId());
    }

    public LocalDate getDateOfTheLastWorkout() {
        User user = userService.getCurrentUser();
        return workoutSessionRepository.findFirstByUserIdOrderByDateDesc(user.getId())
                .map(WorkoutSession::getDate)
                .orElse(null);
    }

    public List<CategoryPercentage> getTopCategoriesForUser() {
        User user = userService.getCurrentUser();

        List<Object[]> results = workoutSessionRepository.findTopCategoriesByUserIdWithSetCount(user.getId());
        results = results.stream().limit(3).toList();

        int totalSets = results.stream()
                .mapToInt(result -> ((Number) result[1]).intValue())
                .sum();

        List<CategoryPercentage> categories = new ArrayList<>();
        for (Object[] result : results) {
            String category = ((CategoryName) result[0]).name();
            int setCount = ((Number) result[1]).intValue();
            double percentage = Math.round((double) setCount / totalSets * 100);


            categories.add(new CategoryPercentage(category, percentage));
        }


        int totalOtherSets = workoutSessionRepository.countAllSetsForUser(user.getId()) - totalSets;

        if (totalOtherSets > 0) {
            double otherPercentage = Math.round((double) totalOtherSets / (totalOtherSets + totalSets) * 100);
            categories.add(new CategoryPercentage("INNE", otherPercentage));
        }

        return categories;
    }

    public Page<WorkoutSessionResponseDTO> getUserWorkouts(int page, int size, String sortBy, String sortDir) {
        User user = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<WorkoutSession> workoutSessionPage = workoutSessionRepository.findAllByUserId(user.getId(), pageable);
        return workoutSessionPage.map(workoutMapper::mapToResponseDTO);
    }

    public void updateWorkout(Long id, WorkoutSessionDTO workoutSessionDTO) {
        User user = userService.getCurrentUser();

        WorkoutSession existingWorkoutSession = workoutSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session not found"));

        if (!existingWorkoutSession.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this workout session");
        }

        existingWorkoutSession.setDate(workoutSessionDTO.date());
        existingWorkoutSession.setNote(workoutSessionDTO.note());


        List<ExerciseSession> existingExerciseSessions = existingWorkoutSession.getExerciseSessions();
        if (existingExerciseSessions == null) {
            existingExerciseSessions = new ArrayList<>();
            existingWorkoutSession.setExerciseSessions(existingExerciseSessions);
        } else {
            existingExerciseSessions.clear();
        }

        for (ExerciseSessionDTO esDTO : workoutSessionDTO.exerciseSessions()) {
            Exercise exercise = exerciseRepository.findById(esDTO.exerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

            ExerciseSession exerciseSession = new ExerciseSession();
            exerciseSession.setExercise(exercise);
            exerciseSession.setWorkoutSession(existingWorkoutSession);

            List<ExerciseSet> sets = esDTO.sets().stream().map(setDTO -> {
                ExerciseSet exerciseSet = new ExerciseSet();
                exerciseSet.setReps(setDTO.reps());
                exerciseSet.setWeight(setDTO.weight());
                exerciseSet.setExerciseSession(exerciseSession);
                return exerciseSet;
            }).collect(Collectors.toList());

            exerciseSession.setSets(sets);

            existingExerciseSessions.add(exerciseSession);
        }

        workoutSessionRepository.save(existingWorkoutSession);
    }


    public void deleteWorkout(Long id) {
        User user = userService.getCurrentUser();

        WorkoutSession existingWorkoutSession = workoutSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session not found"));

        if (!existingWorkoutSession.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this workout session");
        }

        workoutSessionRepository.delete(existingWorkoutSession);
    }
}
