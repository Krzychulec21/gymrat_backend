package com.example.gymrat.mapper;

import com.example.gymrat.DTO.challenge.ChallengeResponseDTO;
import com.example.gymrat.DTO.challenge.MedalInfoDTO;
import com.example.gymrat.model.Challenge;
import org.springframework.stereotype.Component;

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
                challenge.getChallengeParticipants().size()
        );
    }

    public MedalInfoDTO mapToMedalInfoDTO(int gold, int silver, int bronze) {
        return new MedalInfoDTO(gold, silver, bronze);
    }


}
