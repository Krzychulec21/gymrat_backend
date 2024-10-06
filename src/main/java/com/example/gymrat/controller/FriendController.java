package com.example.gymrat.controller;

import com.example.gymrat.DTO.friends.FriendRequestActionDTO;
import com.example.gymrat.DTO.friends.PendingFriendRequestDTO;
import com.example.gymrat.DTO.user.EmailDTO;
import com.example.gymrat.DTO.user.UserResponseDTO;
import com.example.gymrat.DTO.user.UserWithRequestStatusDTO;
import com.example.gymrat.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@AllArgsConstructor
@Tag(name = "Friends", description = "Endpoints for managing friend relationships")
public class FriendController {

    private final FriendService friendService;

    @Operation(summary = "Send a friend request", description = "Sends a friend request to another user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request sent successfully."),
            @ApiResponse(responseCode = "400", description = "Validation errors."),
            @ApiResponse(responseCode = "409", description = "Friend request already exists.")
    })
    @PostMapping("/send-request")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody EmailDTO receiverEmail) {
        String senderEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        friendService.sendFriendRequest(senderEmail, receiverEmail.email());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Respond to a friend request", description = "Accepts or rejects a friend request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request responded successfully."),
            @ApiResponse(responseCode = "403", description = "Access denied."),
            @ApiResponse(responseCode = "404", description = "Friend request not found.")
    })
    @PostMapping("/respond-request")
    @PreAuthorize("@friendService.isReceiverOfRequest(#friendRequestActionDTO.requestId())")
    public ResponseEntity<Void> respondToFriendRequest(@RequestBody FriendRequestActionDTO friendRequestActionDTO) {
        friendService.respondToFriendRequest(friendRequestActionDTO.requestId(), friendRequestActionDTO.accepted());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get friends list", description = "Retrieves the list of friends for the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friends list retrieved successfully.")
    })
    @GetMapping("")
    public ResponseEntity<Page<UserResponseDTO>> getFriends(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<UserResponseDTO> friendsPage = friendService.getFriends(email, page, size, sortBy, sortDir);
        return ResponseEntity.ok(friendsPage);
    }

    @Operation(summary = "Get pending friend requests", description = "Retrieves the list of pending friend requests.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending friend requests retrieved successfully.")
    })
    @GetMapping("/pending-requests")
    public ResponseEntity<List<PendingFriendRequestDTO>> getPendingRequests() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PendingFriendRequestDTO> requests = friendService.getPendingRequests(email);
        return ResponseEntity.ok(requests);
    }

    @Operation(summary = "Remove a friend", description = "Removes a friend from the user's friend list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend removed successfully."),
            @ApiResponse(responseCode = "403", description = "Access denied."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @DeleteMapping("/remove-friend")
    @PreAuthorize("@friendService.areFriends(#friendEmail)")
    public ResponseEntity<Void> removeFriend(@RequestParam String friendEmail) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        friendService.removeFriend(userEmail, friendEmail);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get users with request status", description = "Retrieves all users with their friend request status relative to the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully.")
    })
    @GetMapping("/users-with-request-status")
    public ResponseEntity<List<UserWithRequestStatusDTO>> getUsersWithRequestStatus() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserWithRequestStatusDTO> usersWithStatus = friendService.getUsersWithRequestStatus(currentUserEmail);
        return ResponseEntity.ok(usersWithStatus);
    }

    @Operation(summary = "Search users", description = "Searches for users based on a query string.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found successfully.")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<UserWithRequestStatusDTO>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<UserWithRequestStatusDTO> usersPage = friendService.searchUsersWithRequestStatus(userEmail, query, page, size);
        return ResponseEntity.ok(usersPage);
    }
}
