package com.example.gymrat.controller;

import com.example.gymrat.DTO.auth.AuthenticationRequest;
import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.auth.AuthenticationResponse;
import com.example.gymrat.model.User;
import com.example.gymrat.model.VerificationToken;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.repository.VerificationTokenRepository;
import com.example.gymrat.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        // Sprawdzanie daty wygaśnięcia tokenu
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok("Email successfully verified!");
    }

}
