package com.example.gymrat.controller;

import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
