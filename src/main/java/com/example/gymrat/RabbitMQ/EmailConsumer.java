package com.example.gymrat.RabbitMQ;

import com.example.gymrat.model.EmailMessage;
import com.example.gymrat.model.EmailType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "email-queue")
    public void receiveEmail(EmailMessage message) {
        try {
            if (EmailType.VERIFICATION.equals(message.getEmailType())) {
                emailService.sendVerificationEmail(message.getTo(), message.getVerificationUrl());
            } else if (EmailType.RESET.equals(message.getEmailType())) {
                emailService.sendPasswordResetEmail(message.getTo(), message.getVerificationUrl());
            }
        } catch (MessagingException e) {
            System.err.println("Błąd podczas wysyłania e-maila: " + e.getMessage());
        }
    }
}
