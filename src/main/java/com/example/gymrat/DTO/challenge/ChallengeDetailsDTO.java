package com.example.gymrat.DTO.challenge;

import java.util.List;

public record ChallengeDetailsDTO(
        ChallengeResponseDTO challenge,
        List<ChallengeParticipantResponseDTO> participants,
        boolean isAuthor
) {
}
