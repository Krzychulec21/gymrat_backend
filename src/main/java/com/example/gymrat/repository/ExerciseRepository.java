package com.example.gymrat.repository;

import com.example.gymrat.model.CategoryName;
import com.example.gymrat.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByCategory(CategoryName category);
}
