package com.example.gymrat.repository;

import com.example.gymrat.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiver_EmailAndIsReadFalse(String userEmail);

}
