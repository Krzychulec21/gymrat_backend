package com.example.gymrat.controller;

import com.example.gymrat.DTO.trainingPlan.CommentDTO;
import com.example.gymrat.DTO.trainingPlan.CommentResponseDTO;
import com.example.gymrat.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{trainingPlanId}")
    public ResponseEntity<Void> addComment(@PathVariable Long trainingPlanId, @Valid @RequestBody CommentDTO dto) {
        commentService.addComment(trainingPlanId, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{trainingPlanId}/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long trainingPlanId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDTO dto) {
        commentService.updateComment(trainingPlanId, commentId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{trainingPlanId}/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long trainingPlanId,
            @PathVariable Long commentId) {
        commentService.deleteComment(trainingPlanId, commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{trainingPlanId}")
    public ResponseEntity<Page<CommentResponseDTO>> getComments(
            @PathVariable Long trainingPlanId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(commentService.getComments(trainingPlanId, page, size, sortField, sortDirection));
    }
}
