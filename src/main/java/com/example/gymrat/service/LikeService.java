package com.example.gymrat.service;

import com.example.gymrat.DTO.trainingPlan.LikeDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.model.TrainingPlan;
import com.example.gymrat.model.TrainingPlanLike;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.TrainingPlanLikeRepository;
import com.example.gymrat.repository.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingPlanLikeRepository trainingPlanLikeRepository;
    private final UserService userService;

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

    public int getLikeCount(Long trainingPlanId) {
        trainingPlanRepository.findById(trainingPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

        Integer likeCount = trainingPlanLikeRepository.getLikeCountByTrainingPlanId(trainingPlanId);

        return likeCount != null ? likeCount : 0;
    }
}
