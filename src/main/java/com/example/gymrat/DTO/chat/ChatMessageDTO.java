package com.example.gymrat.DTO.chat;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object representing a chat message")
public record ChatMessageDTO(
        @Schema(description = "Unique identifier of the message", example = "1")
        Long id,

        @Schema(description = "Content of the message", example = "Hello, how are you?")
        String content,

        @Schema(description = "ID of the sender", example = "2")
        Long senderId,

        @Schema(description = "ID of the receiver", example = "3")
        Long receiverId,

        @Schema(description = "Timestamp when the message was sent", example = "2023-10-05T12:30:00")
        LocalDateTime timestamp,

        @Schema(description = "ID of the chat room", example = "4")
        Long chatRoomId
) {}
