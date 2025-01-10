package com.example.gymrat.friends;

import com.example.gymrat.exception.friend.FriendRequestAlreadyExistsException;
import com.example.gymrat.model.FriendRequest;
import com.example.gymrat.model.RequestStatus;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.FriendRequestRepository;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.service.FriendService;
import com.example.gymrat.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendServiceUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendRequestRepository friendRequestRepository;
    @InjectMocks
    private FriendService friendService;

    @Mock
    private NotificationService notificationService;

    @Test
    public void testSendFriendRequestSuccess() {
        User sender = new User();
        sender.setEmail("kowalski@wp.pl");

        User receiver = new User();
        receiver.setEmail("nowak@wp.pl");

        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));

        when(friendRequestRepository.findBySender_EmailAndReceiver_Email(
                sender.getEmail(), receiver.getEmail()))
                .thenReturn(Optional.empty());

        friendService.sendFriendRequest(sender.getEmail(), receiver.getEmail());

        verify(friendRequestRepository, times(1)).save(any(FriendRequest.class));
    }

    @Test
    public void testSendFriendRequestAlreadyExistsThrowsException() {
        User sender = new User();
        sender.setEmail("kowalski@wp.pl");

        User receiver = new User();
        receiver.setEmail("nowak@wp.pl");

        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));

        when(friendRequestRepository.findBySender_EmailAndReceiver_Email(
                sender.getEmail(), receiver.getEmail()))
                .thenReturn(Optional.of(new FriendRequest()));

        assertThrows(FriendRequestAlreadyExistsException.class, () -> {
            friendService.sendFriendRequest(sender.getEmail(), receiver.getEmail());
        });

        verify(friendRequestRepository, never()).save(any(FriendRequest.class));
    }

    @Test
    public void testRespondToFriendRequestAccept() {
        User sender = new User();
        sender.setEmail("kowalski@wp.pl");

        User receiver = new User();
        receiver.setEmail("nowak@wp.pl");

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);

        when(friendRequestRepository.findById(anyLong())).thenReturn(Optional.of(request));

        friendService.respondToFriendRequest(1L, true);

        assertEquals(RequestStatus.ACCEPTED, request.getStatus());
        assertTrue(receiver.getFriends().contains(sender));
        assertTrue(sender.getFriends().contains(receiver));

        verify(friendRequestRepository, times(1)).save(request);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    public void testRespondToFriendRequestReject() {
        User sender = new User();
        sender.setEmail("kowalski@wp.pl");

        User receiver = new User();
        receiver.setEmail("nowak@wp.pl");

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);

        when(friendRequestRepository.findById(anyLong())).thenReturn(Optional.of(request));

        friendService.respondToFriendRequest(1L, false);

        assertEquals(RequestStatus.REJECTED, request.getStatus());


        verify(friendRequestRepository, times(1)).save(request);
        verify(userRepository, never()).save(any(User.class));
    }


}
