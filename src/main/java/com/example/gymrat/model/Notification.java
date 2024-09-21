package com.example.gymrat.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.bytecode.enhance.spi.EnhancementInfo;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User receiver;
    private String content;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean isRead = false;
}
