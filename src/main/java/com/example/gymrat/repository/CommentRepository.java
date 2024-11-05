package com.example.gymrat.repository;

import com.example.gymrat.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByTrainingPlanId(Long trainingPlanId, Pageable pageable);
}
