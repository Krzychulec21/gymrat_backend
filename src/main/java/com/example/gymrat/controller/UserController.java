package com.example.gymrat.controller;

import com.example.gymrat.DTO.user.UserResponseDTO;
import com.example.gymrat.mapper.UserMapper;
import com.example.gymrat.model.User;
import com.example.gymrat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "Endpoints for user information")
public class UserController {

    private final UserService userService;


    @GetMapping("")
    public ResponseEntity<UserResponseDTO> getUser() {
        return ResponseEntity.ok(userService.getUserInfo());
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users.stream().map(user -> new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail())).toList());
    }
}
