package com.example.gymrat.service;

import com.example.gymrat.DTO.friends.FriendResponseDTO;
import com.example.gymrat.DTO.friends.PendingFriendRequestDTO;
import com.example.gymrat.DTO.user.UserResponseDTO;
import com.example.gymrat.DTO.user.UserWithRequestStatusDTO;
import com.example.gymrat.exception.friend.FriendRequestAlreadyExistsException;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.FriendRequestRepository;
import com.example.gymrat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final UserService userService;

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

    public Page<FriendResponseDTO> getFriends(int page, int size, String sortBy, String sortDir, int minAge, int maxAge) {
        User user = userService.getCurrentUser();

        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Sort sort = Sort.by(direction, sortBy.equals("latestMessage") ? "latestMessageTimestamp" : sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Object[]> friendsPage = userRepository.findFriendsWithLatestMessageTimestampAndAgeRange(user.getId(),minAge, maxAge, pageable);

        return friendsPage.map(obj -> {
            User friend = (User) obj[0];
            LocalDateTime latestMessageTimestamp = (LocalDateTime) obj[1];
            return new FriendResponseDTO(
                    friend.getId(),
                    friend.getFirstName(),
                    friend.getLastName(),
                    friend.getEmail(),
                    latestMessageTimestamp,
                    friend.getPersonalInfo().getAvatar()
            );
        });
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

    public Page<UserWithRequestStatusDTO> searchUsersWithRequestStatus(String userEmail, String query, int page, int size) {
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow();
        Pageable pageable = PageRequest.of(page, size);

        Page<User> usersPage = userRepository.searchUsers(query, currentUser.getId(), pageable);

        List<FriendRequest> sentRequests = friendRequestRepository.findBySender(currentUser);

        return usersPage.map(user -> {
            boolean isFriendRequestSent = sentRequests.stream()
                    .anyMatch(friendRequest -> friendRequest.getReceiver().equals(user) && friendRequest.getStatus() == RequestStatus.PENDING);
            return new UserWithRequestStatusDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    isFriendRequestSent
            );
        });
    }
}
