package com.example.gymrat.repository;

import com.example.gymrat.model.ExerciseSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExerciseSessionRepository extends JpaRepository<ExerciseSession, Long> {
    @Query("SELECT es FROM ExerciseSession es " +
            "JOIN es.workoutSession ws " +
            "WHERE es.exercise.id = :exerciseId AND ws.user.id = :userId")
    List<ExerciseSession> findByExerciseIdAndUserId(@Param("exerciseId") Long exerciseId, @Param("userId") Long userId);

}
