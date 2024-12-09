package com.example.gymrat.controller;

import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionResponseDTO;
import com.example.gymrat.model.CategoryCount;
import com.example.gymrat.model.CategoryPercentage;
import com.example.gymrat.model.ExerciseCount;
import com.example.gymrat.model.OneRepMaxDataPoint;
import com.example.gymrat.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping("")
    public ResponseEntity<Long> saveWorkoutSession(@RequestBody WorkoutSessionDTO workoutSessionDTO) {
        return ResponseEntity.ok(workoutService.saveWorkout(workoutSessionDTO));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getNumberOfUserWorkouts() {
        return ResponseEntity.ok(workoutService.getNumberOfUserWorkouts());
    }

    @GetMapping("/total-weight")
    public ResponseEntity<Double> getTotalWeightLiftedByUser() {
        return ResponseEntity.ok(workoutService.getTotalWeightLiftedByUser());
    }

    @GetMapping("/last-workout")
    public ResponseEntity<LocalDate> getDateOfTheLastWorkout() {
        return ResponseEntity.ok(workoutService.getDateOfTheLastWorkout());
    }

    @GetMapping("/top-categories")
    public ResponseEntity<List<CategoryPercentage>> getTopCategoriesForUser() {
        return ResponseEntity.ok(workoutService.getTopCategoriesForUser());
    }

    @GetMapping("/workouts")
    public ResponseEntity<Page<WorkoutSessionResponseDTO>> getUserWorkouts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(workoutService.getUserWorkouts(page, size, sortBy, sortDir));
    }

    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutSessionResponseDTO> getWorkoutById(@PathVariable Long workoutId) {
        return ResponseEntity.ok(workoutService.getWorkoutById(workoutId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWorkoutSession(@PathVariable Long id, @RequestBody WorkoutSessionDTO workoutSessionDTO) {
        workoutService.updateWorkout(id, workoutSessionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutSession(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/trained-categories-count")
    public ResponseEntity<List<CategoryCount>> getTrainedCategoriesCount() {
        return ResponseEntity.ok(workoutService.getTrainedCategoriesCount());
    }

    @GetMapping("/trained-exercises-count")
    public ResponseEntity<List<ExerciseCount>> getTrainedExercisesCount() {
        return ResponseEntity.ok(workoutService.getTrainedExercisesCount());
    }

    @GetMapping("/exercise/{exerciseId}/one-rep-max")
    public ResponseEntity<List<OneRepMaxDataPoint>> getExerciseOneRepMaxProgress(@PathVariable Long exerciseId) {
        return ResponseEntity.ok(workoutService.getExerciseOneRepMaxProgress(exerciseId));
    }


}
