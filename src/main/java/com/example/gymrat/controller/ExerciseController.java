package com.example.gymrat.controller;

import com.example.gymrat.DTO.exercise.CreateExerciseDTO;
import com.example.gymrat.DTO.exercise.ExerciseResponseDTO;
import com.example.gymrat.model.Exercise;
import com.example.gymrat.service.ExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping("")
    public ResponseEntity<List<ExerciseResponseDTO>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }

    @GetMapping("/category")
    public ResponseEntity<List<ExerciseResponseDTO>> getExercisesByCategory(@RequestParam String category) {
        return ResponseEntity.ok(exerciseService.getExercisesByCategory(category));
    }

    @PostMapping("")
    public ResponseEntity<Void> saveExercise(@RequestBody CreateExerciseDTO createExerciseDTO){
        exerciseService.saveExercise(createExerciseDTO);
        return  ResponseEntity.ok().build();
    }
}

