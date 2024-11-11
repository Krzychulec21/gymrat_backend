package com.example.gymrat.repository;

import com.example.gymrat.model.CategoryName;
import com.example.gymrat.model.TrainingPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long>, JpaSpecificationExecutor<TrainingPlan> {
    Page<TrainingPlan> findByAuthorId(Long authorId, Pageable pageable);

    @Query("SELECT DISTINCT tp FROM TrainingPlan tp " +
            "JOIN tp.categories c " +
            "WHERE (:categories IS NULL OR c IN :categories) AND " +
            "(:difficultyLevels IS NULL OR tp.difficultyLevel IN :difficultyLevels) AND " +
            "(:authorNickname IS NULL OR tp.author.nickname = :authorNickname)")
    Page<TrainingPlan> findByFilters(
            @Param("categories") Set<CategoryName> categories,
            @Param("difficultyLevels") List<Integer> difficultyLevels,
            @Param("authorNickname") String authorNickname,
            Pageable pageable
    );
}
