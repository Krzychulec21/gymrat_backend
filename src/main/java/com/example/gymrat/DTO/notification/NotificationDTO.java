package com.example.gymrat.DTO.notification;

import com.example.gymrat.model.NotificationType;

import java.time.LocalDateTime;


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
