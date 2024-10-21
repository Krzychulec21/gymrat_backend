package com.example.gymrat.repository;

import com.example.gymrat.model.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

    int countAllByUserId(Long user_id);

    @Query("SELECT SUM(es.weight * es.reps) FROM ExerciseSet es " +
            "JOIN es.exerciseSession exs " +
            "JOIN exs.workoutSession ws " +
            "WHERE ws.user.id = :userId")
    Double findTotalWeightLiftedByUser(@Param("userId") Long userId);

    Optional<WorkoutSession> findFirstByUserIdOrderByDateDesc(Long userId);

    @Query("SELECT SUM(s.reps) " +
            "FROM ExerciseSession es " +
            "JOIN es.sets s " +
            "WHERE es.workoutSession.user.id = :userId")
    int countAllSetsForUser(@Param("userId") Long userId);


    @Query("SELECT e.category, SUM(s.reps) AS totalSets " +
            "FROM ExerciseSession es " +
            "JOIN es.exercise e " +
            "JOIN es.sets s " +
            "WHERE es.workoutSession.user.id = :userId " +
            "GROUP BY e.category " +
            "ORDER BY totalSets DESC")
    List<Object[]> findTopCategoriesByUserIdWithSetCount(@Param("userId") Long userId);


}
