package com.example.gymrat.controller;

import com.example.gymrat.DTO.trainingPlan.*;
import com.example.gymrat.service.TrainingPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plan")
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    @PostMapping("")
    public ResponseEntity<Void> saveTrainingPlan(@Valid @RequestBody CreateTrainingPlanDTO dto) {
        trainingPlanService.saveTrainingPlan(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingPlanResponseDTO> getTrainingPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(trainingPlanService.getTrainingPlanById(id));
    }

    @GetMapping("")
    public ResponseEntity<Page<TrainingPlanSummaryDTO>> getAllTrainingPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(trainingPlanService.getAllTrainingPlans(page, size, sortField, sortDirection));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TrainingPlanSummaryDTO>> getTrainingPlansByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(trainingPlanService.getTrainingPlansByUser(userId, page, size, sortField, sortDirection));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTrainingPlan(@PathVariable Long id, @Valid @RequestBody UpdateTrainingPlanDTO dto) {
        trainingPlanService.updateTrainingPlan(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingPlan(@PathVariable Long id) {
        trainingPlanService.deleteTrainingPlan(id);
        return ResponseEntity.ok().build();
    }
}
