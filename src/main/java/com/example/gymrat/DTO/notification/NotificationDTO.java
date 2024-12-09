package com.example.gymrat.DTO.notification;

import com.example.gymrat.model.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object representing a notification")
public record NotificationDTO(
        Long id,

        String content,

        LocalDateTime timestamp,

        NotificationType notificationType,

        boolean isRead,

        String senderEmail,

        String senderName,
        Long relatedResourceId
) {
}
