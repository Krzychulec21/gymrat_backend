package com.example.gymrat.controller;

import com.example.gymrat.DTO.exercise.CreateExerciseDTO;
import com.example.gymrat.DTO.exercise.ExerciseInfoResponseDTO;
import com.example.gymrat.DTO.exercise.ExerciseResponseDTO;
import com.example.gymrat.model.CategoryName;
import com.example.gymrat.service.ExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/paginate")
    public ResponseEntity<Page<ExerciseResponseDTO>> getAllExercisesPaginate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Page<ExerciseResponseDTO> exercisePage = exerciseService.getAllExercisesPaginate(page, size, sortBy, sortDir);
        return ResponseEntity.ok(exercisePage);
    }

    @GetMapping("/category")
    public ResponseEntity<List<ExerciseResponseDTO>> getExercisesByCategory(@RequestParam CategoryName category) {
        return ResponseEntity.ok(exerciseService.getExercisesByCategory(category));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Void> saveExercise(@RequestBody CreateExerciseDTO createExerciseDTO) {
        exerciseService.saveExercise(createExerciseDTO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExercise(
            @PathVariable Long id,
            @RequestBody CreateExerciseDTO updateRequest) {
        exerciseService.updateExercise(id, updateRequest);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ExerciseInfoResponseDTO> getExerciseInfo(@PathVariable Long id) {
        return ResponseEntity.ok(exerciseService.getExerciseInfo(id));
    }

    @GetMapping("/trained")
    public ResponseEntity<List<ExerciseResponseDTO>> getExercisesTrainedByUser() {
        return ResponseEntity.ok(exerciseService.getExercisesTrainedByUser());
    }


}

