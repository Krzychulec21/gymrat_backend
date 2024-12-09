package com.example.gymrat.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = true)
    private User sender;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean isRead = false;

    @Column(nullable = true) //only if we need navigate to specific resource like training plan
    private Long relatedResourceId;
}
