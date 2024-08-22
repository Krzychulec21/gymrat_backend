package com.example.gymrat.service;

import com.example.gymrat.DTO.User.auth.AuthenticationRequest;
import com.example.gymrat.DTO.User.auth.RegisterRequest;
import com.example.gymrat.auth.AuthenticationResponse;
import com.example.gymrat.config.JwtService;
import com.example.gymrat.exception.user.UserAlreadyExistsException;
import com.example.gymrat.mapper.UserMapper;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
        }
        if (userRepository.findByNickname(request.nickname()).isPresent()) {
            throw new UserAlreadyExistsException("User with nickname " + request.nickname() + " already exists");
        }
        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public User getUserByNickname(String nickname) {

        return userRepository.findByNickname(nickname).orElseThrow();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = userRepository.findByEmail(request.email()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }






}
