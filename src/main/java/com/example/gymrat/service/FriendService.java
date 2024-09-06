package com.example.gymrat.service;

import com.example.gymrat.DTO.user.UserDTO;
import com.example.gymrat.DTO.user.UserWithRequestStatusDTO;
import com.example.gymrat.exception.friend.FriendRequestAlreadyExistsException;
import com.example.gymrat.model.FriendRequest;
import com.example.gymrat.model.RequestStatus;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.FriendRequestRepository;
import com.example.gymrat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public void sendFriendRequest(String senderEmail, String recipientEmail) {
        User sender = userRepository.findByEmail(senderEmail).orElseThrow();
        User receiver = userRepository.findByEmail(recipientEmail).orElseThrow();

        Optional<FriendRequest> existingRequest = friendRequestRepository.findBySenderAndReceiver(sender.getEmail(), receiver.getEmail());

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
    }

    public void respondToFriendRequest(Long requestId, boolean accepted) {
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow();
        if (accepted) {
            request.setStatus(RequestStatus.ACCEPTED);
            User sender = request.getSender();
            User receiver = request.getReceiver();
            ;
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


    public List<FriendRequest> getPendingRequests(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return friendRequestRepository.findByReceiverAndStatus(user.getEmail(), RequestStatus.PENDING);
    }

    @Transactional
    public void removeFriend(String userEmail, String friendEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        User friend = userRepository.findByEmail(friendEmail).orElseThrow();

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepository.save(user);
        userRepository.save(friend);

        friendRequestRepository.deleteBySenderAndReceiver(user, friend);
        friendRequestRepository.deleteBySenderAndReceiver(friend, user);
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

}
