package com.example.gymrat.service;

import com.example.gymrat.DTO.notification.NotificationDTO;
import com.example.gymrat.model.Notification;
import com.example.gymrat.model.NotificationType;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(User receiver, String message, NotificationType type) {
        // Utwórz obiekt Notification
        Notification notification = new Notification();
        notification.setNotificationType(type);
        notification.setReceiver(receiver);
        notification.setContent(message);
        notificationRepository.save(notification);

        // Utwórz obiekt NotificationDTO do wysłania przez WebSocket
        NotificationDTO notificationDTO = new NotificationDTO(
                notification.getId(),
                notification.getContent(),
                notification.getTimestamp(),
                notification.getNotificationType(),
                notification.isRead()
        );

        // Wyślij obiekt NotificationDTO przez WebSocket
        simpMessagingTemplate.convertAndSendToUser(receiver.getEmail(), "/queue/notifications", notificationDTO);
    }


    public List<NotificationDTO> getUnreadNotifications(String userEmail) {
        List<Notification> notifications = notificationRepository.findByReceiver_EmailAndIsReadFalse(userEmail);
        return notifications.stream().map(notification ->
                new NotificationDTO(
                        notification.getId(),
                        notification.getContent(),
                        notification.getTimestamp(),
                        notification.getNotificationType(),
                        notification.isRead()))
                .collect(Collectors.toList());
    }

    public void markAsRead(List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

}
