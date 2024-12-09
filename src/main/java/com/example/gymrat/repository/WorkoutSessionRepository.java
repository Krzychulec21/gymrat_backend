package com.example.gymrat.repository;

import com.example.gymrat.model.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

    int countAllByUserId(Long user_id);

    Page<WorkoutSession> findAllByUserId(Long user_id, Pageable pageable);

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

    @Query("SELECT e.category, COUNT(DISTINCT ws.date) " +
            "FROM WorkoutSession ws " +
            "JOIN ws.exerciseSessions es " +
            "JOIN es.exercise e " +
            "WHERE ws.user.id = :userId " +
            "GROUP BY e.category " +
            "ORDER BY COUNT(DISTINCT ws.date) DESC")
    List<Object[]> findTrainedCategoriesCount(@Param("userId") Long userId);

    @Query("SELECT e.name, COUNT(DISTINCT ws.date) " +
            "FROM WorkoutSession ws " +
            "JOIN ws.exerciseSessions es " +
            "JOIN es.exercise e " +
            "WHERE ws.user.id = :userId " +
            "GROUP BY e.name " +
            "ORDER BY COUNT(DISTINCT ws.date) DESC")
    List<Object[]> findTrainedExercisesCount(@Param("userId") Long userId);




}
