package com.example.gymrat.repository;


import com.example.gymrat.model.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
    Optional<ChallengeParticipant> findByUserIdAndChallengeId(Long userId, Long challengeId);
}
