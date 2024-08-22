package com.example.gymrat.controller;

import com.example.gymrat.DTO.User.UserCreateDTO;
import com.example.gymrat.DTO.User.auth.AuthenticationRequest;
import com.example.gymrat.DTO.User.auth.RegisterRequest;
import com.example.gymrat.auth.AuthenticationResponse;
import com.example.gymrat.exception.user.UserAlreadyExistsException;
import com.example.gymrat.model.User;
import com.example.gymrat.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        System.out.println("weszlo w endpoint");
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser() {
        // Pobierz aktualnie uwierzytelnionego użytkownika
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Zakładam, że masz metodę do pobierania użytkownika na podstawie emaila (lub innego identyfikatora)
        String userEmail = userDetails.getUsername(); // Lub getEmail(), jeśli masz taką metodę
        User user = userService.findUserByEmail(userEmail);

        return ResponseEntity.ok(user);
    }

}
