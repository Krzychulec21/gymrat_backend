package com.example.gymrat.service;

import com.example.gymrat.DTO.User.UserCreateDTO;
import com.example.gymrat.exception.user.UserAlreadyExistsException;
import com.example.gymrat.exception.user.UserNotFoundException;
import com.example.gymrat.mapper.UserMapper;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(UserCreateDTO userCreateDTO) {
        System.out.println("essa");
        if (userRepository.findByEmail(userCreateDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + userCreateDTO.email() + " already exists");
        }
        if (userRepository.findByUsername(userCreateDTO.username()).isPresent()) {
            throw new UserAlreadyExistsException("User with username " + userCreateDTO.username() + " already exists");
        }
        User user = UserMapper.toEntity(userCreateDTO);
        user.setPassword(passwordEncoder.encode(userCreateDTO.password()));
        return userRepository.save(user);
    }






}
