package com.example.gymrat.controller;

import com.example.gymrat.DTO.trainingPlan.CommentDTO;
import com.example.gymrat.DTO.trainingPlan.CreateTrainingPlanDTO;
import com.example.gymrat.DTO.trainingPlan.LikeDTO;
import com.example.gymrat.DTO.trainingPlan.TrainingPlanResponseDTO;
import com.example.gymrat.service.TrainingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plan")
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    @PostMapping("")
    public ResponseEntity<Void> saveTrainingPlan(@RequestBody CreateTrainingPlanDTO dto) {
        trainingPlanService.saveTrainingPlan(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingPlanResponseDTO> getTrainingPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(trainingPlanService.getTrainingPlanById(id));
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Void> addComment(@PathVariable Long id, @RequestBody CommentDTO dto) {
        trainingPlanService.addComment(id, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> addLike(@PathVariable Long id, @RequestBody LikeDTO dto) {
        trainingPlanService.addLike(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{planId}/comment/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long planId,
            @PathVariable Long commentId,
            @RequestBody CommentDTO dto) {
        trainingPlanService.updateComment(planId, commentId, dto);
        return ResponseEntity.ok().build();
    }


}
