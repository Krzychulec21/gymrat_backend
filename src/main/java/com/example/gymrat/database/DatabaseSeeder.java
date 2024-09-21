package com.example.gymrat.database;

import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.service.ChatService;
import com.example.gymrat.service.FriendService;
import com.example.gymrat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ChatService chatService;
    private final FriendService friendService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedUsers();
        addFriends();
    }


    public void seedUsers() {
        RegisterRequest user1 = new RegisterRequest("Jan", "Kowalski", "kowalczyk", "kowalski@wp.pl", "password");
        RegisterRequest user2 = new RegisterRequest("Michal", "Barylka", "essunia", "nowak@wp.pl", "password");
        RegisterRequest user3 = new RegisterRequest("John", "Doe", "johndoe", "johndoe@xample.com", "password");
        userService.register(user1);
        userService.register(user2);
        userService.register(user3);
    }

    public void addFriends() {
        User user1 = userRepository.findByEmail("kowalski@wp.pl").orElseThrow();
        User user2 = userRepository.findByEmail("nowak@wp.pl").orElseThrow();
        User user3 = userRepository.findByEmail("johndoe@xample.com").orElseThrow();

        friendService.sendFriendRequest(user1.getEmail(), user2.getEmail());
        friendService.sendFriendRequest(user1.getEmail(), user3.getEmail());

        friendService.respondToFriendRequest(1L, true);
        friendService.respondToFriendRequest(2L, true);
    }


}
