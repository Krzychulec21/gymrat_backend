package com.example.gymrat.database;

import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder implements ApplicationRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        seedUsers();
    }

    private void seedUsers() {
        RegisterRequest user1 = new RegisterRequest("Jan", "Kowalski", "kowalczyk", "kowalski@wp.pl", "password");
        RegisterRequest user2 = new RegisterRequest("Michal", "Barylka", "essunia", "nowak@wp.pl", "password");
        userService.register(user1);
        userService.register(user2);
    }
}
