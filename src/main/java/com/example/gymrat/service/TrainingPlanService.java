package com.example.gymrat.service;

import com.example.gymrat.DTO.trainingPlan.*;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.mapper.CommentMapper;
import com.example.gymrat.mapper.TrainingPlanMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.CommentRepository;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.TrainingPlanLikeRepository;
import com.example.gymrat.repository.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final UserService userService;
    private final TrainingPlanMapper trainingPlanMapper;
    private final ExerciseRepository exerciseRepository;
    private final CommentRepository commentRepository;
    private final TrainingPlanLikeRepository trainingPlanLikeRepository;
    private final CommentMapper commentMapper;

    public void saveTrainingPlan(CreateTrainingPlanDTO dto) {
        User currentUser = userService.getCurrentUser();

        List<Long> exerciseIds = dto.exercises().stream()
                .map(ExerciseInPlanDTO::exerciseId)
                .toList();

        List<Exercise> exercises = exerciseRepository.findAllById(exerciseIds);

        //we check here if all passed exercises id exist in database
        if (exercises.size() != exerciseIds.size()) {
            throw new ResourceNotFoundException("One or more exercises not found");
        }
        TrainingPlan trainingPlan = trainingPlanMapper.toEntity(dto, exercises);
        trainingPlan.setAuthor(currentUser);
        trainingPlan.setDifficultyLevel(calculateDifficultyOfTrainingPlan(trainingPlan));

        trainingPlanRepository.save(trainingPlan);
    }

    public TrainingPlanResponseDTO getTrainingPlanById(Long id) {
        TrainingPlan trainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));
        int likeCount = trainingPlan.getLikes().stream()
                .mapToInt(like -> like.getIsLike() ? 1 : -1 )
                .sum();

        return trainingPlanMapper.mapToResponseDTO(trainingPlan, likeCount);
    }

    public int calculateDifficultyOfTrainingPlan(TrainingPlan trainingPlan) {
        return Math.round(
                (float) trainingPlan.getExercisesInPlan()
                        .stream()
                        .map(exerciseInPlan -> exerciseInPlan.getExercise().getExerciseInfo().getDifficultyLevel())
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0)
        );
    }

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

    public void addLike(Long trainingPlanId, LikeDTO dto) {
        User user = userService.getCurrentUser();

        TrainingPlan trainingPlan = trainingPlanRepository.findById(trainingPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

        TrainingPlanLike trainingPlanLike = trainingPlanLikeRepository
                .findTrainingPlanLikeByUserIdAndTrainingPlanId(
                        user.getId(),
                        trainingPlan.getId()
                ).orElse(new TrainingPlanLike());

        if (trainingPlanLike.getIsLike() != null && trainingPlanLike.getIsLike() == dto.isLike()) {
            trainingPlanLikeRepository.delete(trainingPlanLike);
            return;
        }

        trainingPlanLike.setIsLike(dto.isLike());
        trainingPlanLike.setTrainingPlan(trainingPlan);
        trainingPlanLike.setUser(user);

        trainingPlanLikeRepository.save(trainingPlanLike);
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

    public Page<CommentResponseDTO> getComments(Long planId, int page, int size) {
        TrainingPlan trainingPlan = trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findAllByTrainingPlanId(planId, pageable);
        return commentPage.map(commentMapper::mapToDTO);
    }

}
