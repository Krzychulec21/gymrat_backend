package com.example.gymrat.controller;

import com.example.gymrat.DTO.friends.FriendRequestActionDTO;
import com.example.gymrat.DTO.friends.FriendResponseDTO;
import com.example.gymrat.DTO.friends.FriendStatusDTO;
import com.example.gymrat.DTO.friends.PendingFriendRequestDTO;
import com.example.gymrat.DTO.user.EmailDTO;
import com.example.gymrat.DTO.user.UserWithRequestStatusDTO;
import com.example.gymrat.service.FriendService;
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
    public ResponseEntity<Page<FriendResponseDTO>> getFriends(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "18") int minAge,
            @RequestParam(defaultValue = "50") int maxAge) {
        Page<FriendResponseDTO> friendsPage = friendService.getFriends(page, size, sortBy, sortDir, minAge, maxAge);
        return ResponseEntity.ok(friendsPage);
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

    @GetMapping("/search")
    public ResponseEntity<Page<UserWithRequestStatusDTO>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<UserWithRequestStatusDTO> usersPage = friendService.searchUsersWithRequestStatus(userEmail, query, page, size);
        return ResponseEntity.ok(usersPage);
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<FriendStatusDTO> getFriendStatus(@PathVariable Long userId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String status = friendService.getFriendStatus(currentUserEmail, userId);
        return ResponseEntity.ok(new FriendStatusDTO(status));
    }
}
