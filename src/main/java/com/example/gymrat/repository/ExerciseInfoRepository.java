package com.example.gymrat.repository;

import com.example.gymrat.model.ExerciseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseInfoRepository extends JpaRepository<ExerciseInfo, Long> {
    Optional<ExerciseInfo> findByExerciseId(Long exerciseId);

}
