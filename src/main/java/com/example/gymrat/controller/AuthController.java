package com.example.gymrat.controller;

import com.example.gymrat.DTO.auth.AuthenticationRequest;
import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.DTO.auth.ResetPasswordDTO;
import com.example.gymrat.DTO.user.EmailDTO;
import com.example.gymrat.config.AuthenticationResponse;
import com.example.gymrat.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailDTO emailDTO) {
        System.out.println("otryzmano email: " + emailDTO.email());
        userService.sendPasswordResetLink(emailDTO.email());
        return ResponseEntity.ok("Password reset link sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody ResetPasswordDTO dto) {
        userService.updatePassword(token, dto.password());
        return ResponseEntity.ok("Password updated successfully");
    }

}
