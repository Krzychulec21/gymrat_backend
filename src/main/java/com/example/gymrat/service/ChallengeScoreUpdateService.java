package com.example.gymrat.service;

import com.example.gymrat.model.*;
import com.example.gymrat.repository.ChallengeParticipantRepository;
import com.example.gymrat.repository.ChallengeRepository;
import com.example.gymrat.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeScoreUpdateService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final WorkoutSessionRepository workoutSessionRepository;

    public void updateUserChallengesScore(WorkoutSession workoutSession, WorkoutSession oldWorkout) {
        User user = workoutSession.getUser();

        List<ChallengeParticipant> participants = challengeParticipantRepository.findAll().stream()
                .filter(cp -> cp.getUser().getId().equals(user.getId()))
                .filter(cp -> cp.getChallenge().getChallengeStatus() == ChallengeStatus.ACTIVE)
                .toList();

        for (ChallengeParticipant participant : participants) {
            Challenge challenge = participant.getChallenge();
            ChallengeType type = challenge.getChallengeType();

            switch (type.getName()) {
                case "Najdluzsza passa treningu", "Najdłuższa passa treningu" ->
                        updateLongestStreakChallenge(participant, workoutSession);
                case "Najwięcej przerzuconych kilogramów" ->
                        updateMostWeightLiftedChallenge(participant, workoutSession, oldWorkout);
                case "Najsilniejszy na raz" -> updateStrongestSingleChallenge(participant, workoutSession);
                default -> {
                }
            }

            challengeParticipantRepository.save(participant);
        }
    }

    private void updateLongestStreakChallenge(ChallengeParticipant participant, WorkoutSession newSession) {
        User user = participant.getUser();

        LocalDate currentDate = newSession.getDate();
        LocalDate yesterday = currentDate.minusDays(1);

        boolean trainedYesterday = workoutSessionRepository
                .findFirstByUserIdAndDateOrderByDateDesc(user.getId(), yesterday)
                .isPresent();

        if (trainedYesterday) {
            participant.setScore(participant.getScore() + 1);
        } else {
            participant.setScore(1.0);
        }
    }


    private void updateMostWeightLiftedChallenge(ChallengeParticipant participant, WorkoutSession newSession, WorkoutSession oldWorkout) {
        Challenge challenge = participant.getChallenge();
        Exercise challengeExercise = challenge.getExercise();
        double oldWeight = 0;

        if (challengeExercise == null) {
            return;
        }

        if (oldWorkout != null) {
            oldWeight = oldWorkout.getExerciseSessions().stream()
                    .filter(es -> es.getExercise().getId().equals(challengeExercise.getId()))
                    .flatMap(es -> es.getSets().stream())
                    .mapToDouble(set -> set.getWeight() * set.getReps())
                    .sum();
        }


        double totalWeight = newSession.getExerciseSessions().stream()
                .filter(es -> es.getExercise().getId().equals(challengeExercise.getId()))
                .flatMap(es -> es.getSets().stream())
                .mapToDouble(set -> set.getWeight() * set.getReps())
                .sum();

        participant.setScore(participant.getScore() + (totalWeight - oldWeight));
    }


    private void updateStrongestSingleChallenge(ChallengeParticipant participant, WorkoutSession newSession) {
        Challenge challenge = participant.getChallenge();
        Exercise challengeExercise = challenge.getExercise();
        if (challengeExercise == null) {
            return;
        }

        double maxWeight = newSession.getExerciseSessions().stream()
                .filter(es -> es.getExercise().getId().equals(challengeExercise.getId()))
                .flatMap(es -> es.getSets().stream())
                .mapToDouble(set -> set.getWeight())
                .max()
                .orElse(0);

        if (maxWeight > participant.getScore()) {
            participant.setScore(maxWeight);
        }
    }
}
