package com.example.gymrat.repository;

import com.example.gymrat.model.TrainingPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    Page<TrainingPlan> findByAuthorId(Long authorId, Pageable pageable);
}
