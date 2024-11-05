package com.example.gymrat.service;

import com.example.gymrat.DTO.trainingPlan.CommentDTO;
import com.example.gymrat.DTO.trainingPlan.CommentResponseDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.mapper.CommentMapper;
import com.example.gymrat.model.Comment;
import com.example.gymrat.model.TrainingPlan;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.CommentRepository;
import com.example.gymrat.repository.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TrainingPlanRepository trainingPlanRepository;
    private final UserService userService;
    private final CommentMapper commentMapper;

    public void addComment(Long trainingPlanId, CommentDTO dto) {
        User user = userService.getCurrentUser();

        TrainingPlan trainingPlan = trainingPlanRepository.findById(trainingPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setTrainingPlan(trainingPlan);
        comment.setContent(dto.content());

        commentRepository.save(comment);
    }

    public void updateComment(Long planId, Long commentId, CommentDTO dto) {
        User user = userService.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with given ID does not exist"));

        if (!comment.getTrainingPlan().getId().equals(planId)) {
            throw new ResourceNotFoundException("Comment does not belong to the specified training plan");
        }

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only edit your own comments");
        }

        comment.setContent(dto.content());
        commentRepository.save(comment);
    }

    public void deleteComment(Long planId, Long commentId) {
        User user = userService.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with given ID does not exist"));

        if (!comment.getTrainingPlan().getId().equals(planId)) {
            throw new ResourceNotFoundException("Comment does not belong to the specified training plan");
        }

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    public Page<CommentResponseDTO> getComments(Long planId, int page, int size, String sortField, String sortDirection) {
        trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Comment> commentPage = commentRepository.findAllByTrainingPlanId(planId, pageable);

        return commentPage.map(commentMapper::mapToDTO);
    }
}
