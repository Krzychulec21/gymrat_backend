package com.example.gymrat.repository;

import com.example.gymrat.model.FavoriteTrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteTrainingPlanRepository extends JpaRepository<FavoriteTrainingPlan, Long> {
    Optional<FavoriteTrainingPlan> findByUserIdAndTrainingPlanId(Long userId, Long trainingPlanId);

    List<FavoriteTrainingPlan> findByUserId(Long userId);
}
