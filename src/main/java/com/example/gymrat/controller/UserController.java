package com.example.gymrat.controller;

import com.example.gymrat.DTO.admin.WarnMessageDTO;
import com.example.gymrat.DTO.user.UserResponseDTO;
import com.example.gymrat.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(userService.getAllUsers(page, size, sortBy, sortDir));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        UserResponseDTO userDTO = userService.getUserInfo(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{userId}/warn")
    public ResponseEntity<String> sendWarnToUser(
            @PathVariable Long userId,
            @RequestBody WarnMessageDTO dto
    ) {
        userService.sendWarnToUser(userId, dto);
        return ResponseEntity.ok("Send warning to user successfully");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{userId}/block")
    public ResponseEntity<String> sendWarnToUser(
            @PathVariable Long userId
    ) {
        userService.blockUser(userId);
        return ResponseEntity.ok("User has benn blocked successfully");
    }


}
