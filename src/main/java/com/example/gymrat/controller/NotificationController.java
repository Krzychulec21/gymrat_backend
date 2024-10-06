package com.example.gymrat.controller;

import com.example.gymrat.DTO.notification.NotificationDTO;
import com.example.gymrat.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notifications", description = "Endpoints for managing notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "Get unread notifications", description = "Retrieves unread notifications for the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unread notifications retrieved successfully.")
    })
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userEmail));
    }

    @Operation(summary = "Mark notifications as read", description = "Marks specified notifications as read.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications marked as read successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    @PostMapping("/read")
    public ResponseEntity<Void> markAsRead(@RequestBody List<Long> notificationIds) {
        notificationService.markAsRead(notificationIds);
        return ResponseEntity.ok().build();
    }
}
