package com.example.gymrat.repository;

import com.example.gymrat.model.CategoryName;
import com.example.gymrat.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByCategory(CategoryName category);

    Optional<Exercise> findByName(String name);

    @Query("SELECT DISTINCT e FROM ExerciseSession es " +
            "JOIN es.exercise e " +
            "JOIN es.workoutSession ws " +
            "WHERE ws.user.id = :userId")
    List<Exercise> findExercisesTrainedByUser(@Param("userId") Long userId);

}
