package com.example.gymrat.controller;

import com.example.gymrat.model.User;
import com.example.gymrat.model.VerificationToken;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class EmailController {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, Model model) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("message", "Token wygasł. Proszę spróbować ponownie.");
            return "verification-result";
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        model.addAttribute("message", "E-mail został pomyślnie zweryfikowany!");
        return "verification-result";
    }
}
