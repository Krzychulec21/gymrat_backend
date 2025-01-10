package com.example.gymrat.controller;

import com.example.gymrat.DTO.trainingPlan.CreateTrainingPlanDTO;
import com.example.gymrat.DTO.trainingPlan.TrainingPlanResponseDTO;
import com.example.gymrat.DTO.trainingPlan.TrainingPlanSummaryDTO;
import com.example.gymrat.DTO.trainingPlan.UpdateTrainingPlanDTO;
import com.example.gymrat.model.CategoryName;
import com.example.gymrat.service.TrainingPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<Page<TrainingPlanSummaryDTO>> getAllTrainingPlansSummary(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) Set<CategoryName> categories,
            @RequestParam(required = false) List<Integer> difficultyLevels,
            @RequestParam(required = false) String authorNickname,
            @RequestParam(required = false) Boolean onlyFavorites
    ) {
        return ResponseEntity.ok(
                trainingPlanService.getAllTrainingPlans(page, size, sortField, sortDirection, categories, difficultyLevels, authorNickname, onlyFavorites)
        );
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

    @PostMapping("/{id}/favorite")
    public ResponseEntity<Void> toggleFavoriteTrainingPlan(@PathVariable Long id) {
        trainingPlanService.toggleFavorite(id);
        return ResponseEntity.ok().build();
    }
}
