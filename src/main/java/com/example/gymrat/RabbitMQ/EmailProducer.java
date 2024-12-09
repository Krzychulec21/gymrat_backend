package com.example.gymrat.RabbitMQ;

import com.example.gymrat.model.EmailMessage;
import com.example.gymrat.model.EmailType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendVerificationEmail(String to, String verificationUrl) {
        EmailMessage message = new EmailMessage(to, verificationUrl, EmailType.VERIFICATION);
        rabbitTemplate.convertAndSend("email-queue", message);
    }

    public void sendResetPasswordEmail(String to, String verificationUrl) {
        EmailMessage message = new EmailMessage(to, verificationUrl, EmailType.RESET);
        rabbitTemplate.convertAndSend("email-queue", message);
    }


}
