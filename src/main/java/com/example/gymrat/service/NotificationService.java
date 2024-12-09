package com.example.gymrat.service;

import com.example.gymrat.DTO.notification.NotificationDTO;
import com.example.gymrat.model.Notification;
import com.example.gymrat.model.NotificationType;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(User receiver, User sender, String message, NotificationType type, Long relatedResourceId) {
        Notification notification = new Notification();
        notification.setNotificationType(type);
        notification.setReceiver(receiver);
        notification.setSender(sender);
        notification.setContent(message);
        notificationRepository.save(notification);

        NotificationDTO notificationDTO = new NotificationDTO(
                notification.getId(),
                notification.getContent(),
                notification.getTimestamp(),
                notification.getNotificationType(),
                notification.isRead(),
                sender != null ? sender.getEmail() : null,
                sender != null ? sender.getFirstName() + " " + sender.getLastName() : null,
                relatedResourceId
        );

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
                                notification.isRead(),
                                notification.getSender() != null ? notification.getSender().getEmail() : null,
                                notification.getSender() != null ? notification.getSender().getFirstName() + " " + notification.getSender().getLastName() : null,
                                notification.getRelatedResourceId() != null ? notification.getRelatedResourceId() : null))
                .collect(Collectors.toList());
    }

    public void markAsRead(List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

}
