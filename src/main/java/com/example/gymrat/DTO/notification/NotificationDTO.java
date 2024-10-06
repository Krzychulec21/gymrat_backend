package com.example.gymrat.DTO.notification;

import com.example.gymrat.model.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object representing a notification")
public record NotificationDTO(
        @Schema(description = "Unique identifier of the notification", example = "1")
        Long id,

        @Schema(description = "Content of the notification", example = "You have a new friend request")
        String content,

        @Schema(description = "Timestamp of the notification", example = "2023-10-05T12:30:00")
        LocalDateTime timestamp,

        @Schema(description = "Type of the notification", example = "FRIEND_REQUEST")
        NotificationType notificationType,

        @Schema(description = "Indicates if the notification has been read", example = "false")
        boolean isRead,

        @Schema(description = "Email of the sender", example = "sender@example.com")
        String senderEmail,

        @Schema(description = "Name of the sender", example = "John Doe")
        String senderName
) {}
