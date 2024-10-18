package com.example.gymrat.service;

import com.example.gymrat.DTO.workout.ExerciseSessionDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.model.Exercise;
import com.example.gymrat.model.ExerciseSession;
import com.example.gymrat.model.ExerciseSet;
import com.example.gymrat.model.WorkoutSession;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.ExerciseSessionRepository;
import com.example.gymrat.repository.ExerciseSetRepository;
import com.example.gymrat.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private WorkoutSession mapToEntity(WorkoutSessionDTO workoutSessionDTO) {
        System.out.println("otrzymane wokrout session: " + workoutSessionDTO);
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



    public void saveWorkout(WorkoutSessionDTO workoutSessionDTO) {
        WorkoutSession workoutSession = mapToEntity(workoutSessionDTO);
        workoutSessionRepository.save(workoutSession);
    }


}
