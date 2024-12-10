package com.example.gymrat.repository;

import com.example.gymrat.model.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Page<Challenge> findAll(Specification<Challenge> specification, Pageable pageable);

    List<Challenge> findAll(Specification<Challenge> specification);
}
