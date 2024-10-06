package com.example.gymrat.controller;

import com.example.gymrat.DTO.user.UserResponseDTO;
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

    @Operation(summary = "Get current user info", description = "Retrieves information about the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")
    })
    @GetMapping("")
    public ResponseEntity<UserResponseDTO> getUser() {
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully.")
    })
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
