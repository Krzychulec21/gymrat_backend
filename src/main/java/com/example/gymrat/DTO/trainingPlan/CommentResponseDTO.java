package com.example.gymrat.DTO.trainingPlan;

import java.time.LocalDateTime;

public record CommentResponseDTO(
        Long id,
        String content,
        String authorNickname,
        LocalDateTime dateCreated

) {
}
