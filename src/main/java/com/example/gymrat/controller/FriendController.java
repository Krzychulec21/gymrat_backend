package com.example.gymrat.controller;

import com.example.gymrat.DTO.friends.FriendRequestActionDTO;
import com.example.gymrat.DTO.friends.PendingFriendRequestDTO;
import com.example.gymrat.DTO.user.EmailDTO;
import com.example.gymrat.DTO.user.UserDTO;
import com.example.gymrat.DTO.user.UserWithRequestStatusDTO;
import com.example.gymrat.model.FriendRequest;
import com.example.gymrat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/send-request")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody EmailDTO receiverEmail) {
        String senderEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        friendService.sendFriendRequest(senderEmail, receiverEmail.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/respond-request")
    public ResponseEntity<Void> respondToFriendRequest(@RequestBody FriendRequestActionDTO friendRequestActionDTO) {
        friendService.respondToFriendRequest(friendRequestActionDTO.requestId(), friendRequestActionDTO.accepted());
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getFriends() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserDTO> friends = friendService.getFriends(email);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<List<PendingFriendRequestDTO>> getPendingRequests() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PendingFriendRequestDTO> requests = friendService.getPendingRequests(email);
        return ResponseEntity.ok(requests);
    }

    @DeleteMapping("/remove-friend")
    public ResponseEntity<Void> removeFriend(@RequestBody EmailDTO friendEmail) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        friendService.removeFriend(userEmail, friendEmail.email());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users-with-request-status")
    public ResponseEntity<List<UserWithRequestStatusDTO>> getUsersWithRequestStatus() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserWithRequestStatusDTO> usersWithStatus = friendService.getUsersWithRequestStatus(currentUserEmail);
        return ResponseEntity.ok(usersWithStatus);
    }

}
