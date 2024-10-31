package com.example.gymrat.mapper;

import com.example.gymrat.DTO.workout.ExerciseSessionDTO;
import com.example.gymrat.DTO.workout.ExerciseSetDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionResponseDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.ExerciseRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class WorkoutMapper {

    private final ExerciseRepository exerciseRepository; //It's not recommended to use repository in mapper (I should change this)

    public WorkoutSession mapToEntity(WorkoutSessionDTO workoutSessionDTO) {
        WorkoutSession workoutSession = new WorkoutSession();
        workoutSession.setDate(workoutSessionDTO.date());
        workoutSession.setNote(workoutSessionDTO.note());

        List<ExerciseSession> exerciseSessions = workoutSessionDTO.exerciseSessions()
                .stream()
                .map(esDTO -> {
                    Exercise exercise = exerciseRepository.findById(esDTO.exerciseId())
                            .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

                    ExerciseSession exerciseSession = new ExerciseSession();
                    exerciseSession.setExercise(exercise);
                    exerciseSession.setWorkoutSession(workoutSession);

                    List<ExerciseSet> sets = esDTO.sets().stream().map(setDTO -> {
                        ExerciseSet exerciseSet = new ExerciseSet();
                        exerciseSet.setReps(setDTO.reps());
                        exerciseSet.setWeight(setDTO.weight());
                        exerciseSet.setExerciseSession(exerciseSession);
                        return exerciseSet;
                    }).collect(Collectors.toList());

                    exerciseSession.setSets(sets);
                    return exerciseSession;
                }).collect(Collectors.toList());

        workoutSession.setExerciseSessions(exerciseSessions);
        return workoutSession;
    }

    public WorkoutSessionResponseDTO mapToResponseDTO(WorkoutSession workoutSession) {
        int numberOfExercises = workoutSession.getExerciseSessions().size();

        Map<CategoryName, Integer> categoryCount = new HashMap<>();
        for (ExerciseSession es : workoutSession.getExerciseSessions()) {
            CategoryName category = es.getExercise().getCategory();
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
        }

        CategoryName mainCategory = categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return new WorkoutSessionResponseDTO(
                workoutSession.getId(),
                workoutSession.getDate(),
                workoutSession.getNote(),
                numberOfExercises,
                mainCategory != null ? mainCategory.name() : null,
                workoutSession.getExerciseSessions().stream()
                        .map(exerciseSession -> new ExerciseSessionDTO(
                                exerciseSession.getExercise().getId(),
                                exerciseSession.getExercise().getName(),
                                exerciseSession.getSets().stream()
                                        .map(exerciseSet -> new ExerciseSetDTO(
                                                exerciseSet.getReps(),
                                                exerciseSet.getWeight()
                                        ))
                                        .collect(Collectors.toList())
                        )).collect(Collectors.toList())
        );
    }
}
