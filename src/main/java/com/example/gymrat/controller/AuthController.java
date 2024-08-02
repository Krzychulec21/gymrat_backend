package com.example.gymrat.controller;

import com.example.gymrat.DTO.User.UserCreateDTO;
import com.example.gymrat.exception.user.UserAlreadyExistsException;
import com.example.gymrat.model.User;
import com.example.gymrat.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String register() {
        System.out.println("register called");
        return "register";
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        System.out.println("registerUser called with: " + userCreateDTO);
        try{
            User user = userService.saveUser(userCreateDTO);
            return ResponseEntity.ok(user);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

}
