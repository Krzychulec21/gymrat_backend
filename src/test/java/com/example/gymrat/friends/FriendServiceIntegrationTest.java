package com.example.gymrat.friends;

import com.example.gymrat.model.FriendRequest;
import com.example.gymrat.model.RequestStatus;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.FriendRequestRepository;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.service.FriendService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FriendServiceIntegrationTest {

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Test
    public void testSendFriendRequest() {
        User sender = new User();
        sender.setEmail("sender@wp.pl");
        userRepository.save(sender);

        User receiver = new User();
        receiver.setEmail("receiver@wp.pl");
        userRepository.save(receiver);

        friendService.sendFriendRequest(sender.getEmail(), receiver.getEmail());

        Optional<FriendRequest> request = friendRequestRepository.findBySender_EmailAndReceiver_Email(sender.getEmail(), receiver.getEmail());
        assertTrue(request.isPresent());
        assertEquals(RequestStatus.PENDING, request.get().getStatus());
    }
}