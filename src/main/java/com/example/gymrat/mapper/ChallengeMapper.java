package com.example.gymrat.mapper;

import com.example.gymrat.DTO.challenge.*;
import com.example.gymrat.model.Challenge;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ChallengeMapper {

    public ChallengeResponseDTO mapToDto(Challenge challenge) {
        return new ChallengeResponseDTO(
                challenge.getId(),
                challenge.getName(),
                challenge.getChallengeType().getName(),
                challenge.getChallengeStatus().name(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                challenge.isPublic(),
                challenge.getChallengeParticipants().size(),
                challenge.getExercise() != null ? challenge.getExercise().getName() : null,
                challenge.getAuthor().getNickname()
        );
    }

    public MedalInfoDTO mapToMedalInfoDTO(int gold, int silver, int bronze) {
        return new MedalInfoDTO(gold, silver, bronze);
    }


    public UserRankingDTO mapToUserRankingDTO(Long userId, String nickname, int gold, int silver, int bronze, int totalPoints, int rank) {
        return new UserRankingDTO(userId, nickname, gold, silver, bronze, totalPoints, rank);
    }

    public ActiveChallengeDTO mapToActiveChallengeDTO(Challenge challenge) {
        long daysLeft = challenge.getEndDate().toEpochDay() - LocalDate.now().toEpochDay();
        return new ActiveChallengeDTO(
                challenge.getId(),
                challenge.getName(),
                daysLeft,
                challenge.getChallengeParticipants().size(),
                challenge.isPublic()
        );
    }

    public AvailableChallengeDTO mapToAvailableChallengeDTO(Challenge challenge) {
        long daysLeft = challenge.getEndDate().toEpochDay() - LocalDate.now().toEpochDay();
        String exerciseName = challenge.getChallengeType().getName().equalsIgnoreCase("passa")
                ? null
                : (challenge.getExercise() != null ? challenge.getExercise().getName() : null);

        return new AvailableChallengeDTO(
                challenge.getId(),
                challenge.getAuthor().getNickname(),
                challenge.getChallengeType().getName(),
                exerciseName,
                daysLeft,
                challenge.getChallengeParticipants().size(),
                challenge.isPublic()
        );
    }

}
