package com.example.gymrat.controller;

import com.example.gymrat.DTO.trainingPlan.LikeDTO;
import com.example.gymrat.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{trainingPlanId}")
    public ResponseEntity<Void> addLike(@PathVariable Long trainingPlanId, @Valid @RequestBody LikeDTO dto) {
        likeService.addLike(trainingPlanId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{trainingPlanId}/count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long trainingPlanId) {
        return ResponseEntity.ok(likeService.getLikeCount(trainingPlanId));
    }
}
