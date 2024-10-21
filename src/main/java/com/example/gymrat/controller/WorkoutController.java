package com.example.gymrat.controller;

import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.model.CategoryPercentage;
import com.example.gymrat.service.WorkoutService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Void> saveWorkoutSession(@RequestBody WorkoutSessionDTO workoutSessionDTO) {
        workoutService.saveWorkout(workoutSessionDTO);
        return ResponseEntity.ok().build();
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
    public ResponseEntity <LocalDate> getDateOfTheLastWorkout() {
        return ResponseEntity.ok(workoutService.getDateOfTheLastWorkout());
    }

    @GetMapping("/top-categories")
    public ResponseEntity<List<CategoryPercentage>> getTopCategoriesForUser() {
        return ResponseEntity.ok(workoutService.getTopCategoriesForUser());
    }
}
