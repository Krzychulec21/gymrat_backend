package com.example.gymrat.controller;

import com.example.gymrat.DTO.friends.FriendRequestActionDTO;
import com.example.gymrat.DTO.friends.PendingFriendRequestDTO;
import com.example.gymrat.DTO.user.EmailDTO;
import com.example.gymrat.DTO.user.UserResponseDTO;
import com.example.gymrat.DTO.user.UserWithRequestStatusDTO;
import com.example.gymrat.service.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@AllArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/send-request")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody EmailDTO receiverEmail) {
        String senderEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        friendService.sendFriendRequest(senderEmail, receiverEmail.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/respond-request")
    @PreAuthorize("@friendService.isReceiverOfRequest(#friendRequestActionDTO.requestId())")
    public ResponseEntity<Void> respondToFriendRequest(@RequestBody FriendRequestActionDTO friendRequestActionDTO) {
        friendService.respondToFriendRequest(friendRequestActionDTO.requestId(), friendRequestActionDTO.accepted());
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponseDTO>> getFriends() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserResponseDTO> friends = friendService.getFriends(email);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<List<PendingFriendRequestDTO>> getPendingRequests() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PendingFriendRequestDTO> requests = friendService.getPendingRequests(email);
        return ResponseEntity.ok(requests);
    }

    @DeleteMapping("/remove-friend")
    @PreAuthorize("@friendService.areFriends(#friendEmail)")
    public ResponseEntity<Void> removeFriend(@RequestParam String friendEmail) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        friendService.removeFriend(userEmail, friendEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users-with-request-status")
    public ResponseEntity<List<UserWithRequestStatusDTO>> getUsersWithRequestStatus() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserWithRequestStatusDTO> usersWithStatus = friendService.getUsersWithRequestStatus(currentUserEmail);
        return ResponseEntity.ok(usersWithStatus);
    }

}
