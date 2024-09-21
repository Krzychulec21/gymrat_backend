package com.example.gymrat.controller;

import com.example.gymrat.DTO.notification.NotificationDTO;
import com.example.gymrat.model.Notification;
import com.example.gymrat.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userEmail));
    }

    @PostMapping("/read")
    public ResponseEntity<Void> markAsRead(@RequestBody List<Long> notificationIds) {
        notificationService.markAsRead(notificationIds);
        return ResponseEntity.ok().build();
    }
}
