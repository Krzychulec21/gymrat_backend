package com.example.gymrat.service;

import com.example.gymrat.DTO.workout.ExerciseSessionDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.ExerciseSessionRepository;
import com.example.gymrat.repository.ExerciseSetRepository;
import com.example.gymrat.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
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


    public WorkoutSession mapToEntity(WorkoutSessionDTO workoutSessionDTO) {
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
        User user = userService.getCurrentUser();
        workoutSession.setUser(user);
        workoutSessionRepository.save(workoutSession);
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

    public List<CategoryPercentage> getTopCategoriesForUser () {
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
            double percentage = (double) setCount / totalSets * 100;

            categories.add(new CategoryPercentage(category, percentage));
        }


        int totalOtherSets = workoutSessionRepository.countAllSetsForUser(user.getId()) - totalSets;

        if (totalOtherSets > 0) {
            double otherPercentage = (double) totalOtherSets / (totalOtherSets + totalSets) * 100;
            categories.add(new CategoryPercentage("OTHER", otherPercentage));
        }

        return categories;
    }
}
