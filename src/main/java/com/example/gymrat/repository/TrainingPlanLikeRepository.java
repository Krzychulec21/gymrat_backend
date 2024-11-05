package com.example.gymrat.repository;

import com.example.gymrat.model.TrainingPlanLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TrainingPlanLikeRepository extends JpaRepository<TrainingPlanLike, Long> {
    Optional<TrainingPlanLike> findTrainingPlanLikeByUserIdAndTrainingPlanId(Long userId, Long trainingPlanId);

    @Query("SELECT SUM(CASE WHEN tpl.isLike = true THEN 1 ELSE -1 END) FROM TrainingPlanLike tpl WHERE tpl.trainingPlan.id = :trainingPlanId")
    Integer getLikeCountByTrainingPlanId(Long trainingPlanId);
}
