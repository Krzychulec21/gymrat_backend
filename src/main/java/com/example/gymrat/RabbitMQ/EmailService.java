package com.example.gymrat.RabbitMQ;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendVerificationEmail(String to, String verificationUrl) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");


        Context context = new Context();
        context.setVariable("verificationUrl", verificationUrl);


        String htmlContent = templateEngine.process("email-verification", context);

        helper.setTo(to);
        helper.setSubject("Zweryfikuj swoje konto");
        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }

    public void sendPasswordResetEmail(String to, String resetUrl) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");


        Context context = new Context();
        context.setVariable("resetUrl", resetUrl);


        String htmlContent = templateEngine.process("password-reset", context);

        helper.setTo(to);
        helper.setSubject("Reset hasła");
        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }


}