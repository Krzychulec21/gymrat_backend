package com.example.gymrat.service;

import com.example.gymrat.DTO.friends.PendingFriendRequestDTO;
import com.example.gymrat.DTO.user.UserDTO;
import com.example.gymrat.DTO.user.UserWithRequestStatusDTO;
import com.example.gymrat.exception.friend.FriendRequestAlreadyExistsException;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.FriendRequestRepository;
import com.example.gymrat.repository.NotificationRepository;
import com.example.gymrat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public void sendFriendRequest(String senderEmail, String recipientEmail) {
        User sender = userRepository.findByEmail(senderEmail).orElseThrow();
        User receiver = userRepository.findByEmail(recipientEmail).orElseThrow();

        Optional<FriendRequest> existingRequest = friendRequestRepository.findBySender_EmailAndReceiver_Email(sender.getEmail(), receiver.getEmail());

        if (existingRequest.isPresent()) {
            FriendRequest request = existingRequest.get();
            if (request.getStatus() != RequestStatus.REJECTED) {
                throw new FriendRequestAlreadyExistsException("Friend request is already pending or accepted.");
            }
            request.setStatus(RequestStatus.PENDING);
            friendRequestRepository.save(request);
        } else {
            FriendRequest newRequest = new FriendRequest();
            newRequest.setSender(sender);
            newRequest.setReceiver(receiver);
            newRequest.setStatus(RequestStatus.PENDING);
            friendRequestRepository.save(newRequest);
        }

        notificationService.sendNotification(receiver,sender, "Nowe zaproszenie od "+ sender.getFirstName() +" " + sender.getLastName(), NotificationType.FRIEND_REQUEST);
    }

    public void respondToFriendRequest(Long requestId, boolean accepted) {
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow();
        if (accepted) {
            request.setStatus(RequestStatus.ACCEPTED);
            User sender = request.getSender();
            User receiver = request.getReceiver();
            sender.getFriends().add(receiver);
            receiver.getFriends().add(sender);
            userRepository.save(sender);
            userRepository.save(receiver);
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }
        friendRequestRepository.save(request);
    }

    public List<UserDTO> getFriends(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return user.getFriends().stream()
                .map(friend -> new UserDTO(friend.getId(), friend.getFirstName(), friend.getLastName(), friend.getEmail()))
                .collect(Collectors.toList());
    }


    public List<PendingFriendRequestDTO> getPendingRequests(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return friendRequestRepository.findByReceiver_EmailAndStatus(user.getEmail(), RequestStatus.PENDING);
    }

    @Transactional
    public void removeFriend(String userEmail, String friendEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        User friend = userRepository.findByEmail(friendEmail).orElseThrow();

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepository.save(user);
        userRepository.save(friend);

        friendRequestRepository.deleteAllBetweenUsers(user.getId(), friend.getId());
    }

    public List<UserWithRequestStatusDTO> getUsersWithRequestStatus(String currentUserEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow();

        List<User> allUsers = userRepository.findAll();
        List<FriendRequest> sentRequests = friendRequestRepository.findBySender(currentUser);

        return allUsers.stream()
                .filter(user -> !user.getEmail().equals(currentUserEmail))
                .map(user -> {
                    boolean isRequestSent = sentRequests.stream()
                            .anyMatch(request -> request.getReceiver().equals(user));
                    return new UserWithRequestStatusDTO(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            isRequestSent
                    );
                })
                .collect(Collectors.toList());
    }

    public boolean isReceiverOfRequest(Long requestId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        FriendRequest request = friendRequestRepository.findById(requestId).orElse(null);
        if (request == null) {
            return false;
        }
        return request.getReceiver().getEmail().equals(userEmail);
    }

    public boolean areFriends(String friendEmail) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail).orElse(null);
        User friend = userRepository.findByEmail(friendEmail).orElse(null);
        if (user == null || friend == null) {
            return false;
        }
        return user.getFriends().contains(friend);
    }

}
