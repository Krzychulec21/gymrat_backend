package com.example.gymrat.repository;

import com.example.gymrat.model.ExerciseSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseSessionRepository extends JpaRepository<ExerciseSession, Long> {
}
