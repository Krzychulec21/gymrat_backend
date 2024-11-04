package com.example.gymrat.repository;

import com.example.gymrat.model.TrainingPlanLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingPlanLikeRepository extends JpaRepository<TrainingPlanLike, Long> {
    Optional<TrainingPlanLike> findTrainingPlanLikeByUserIdAndTrainingPlanId(Long userId, Long trainingPlanId);

}
