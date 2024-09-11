package com.example.gymrat.DTO.chat;

import java.time.LocalDateTime;

public record ChatMessageDTO (
        Long id,
        String content,
        Long senderId,
        Long receiverId,
        LocalDateTime timestamp,
        Long chatRoomId

) {
}
