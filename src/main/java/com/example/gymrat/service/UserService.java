package com.example.gymrat.service;

import com.example.gymrat.DTO.auth.AuthenticationRequest;
import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.DTO.user.UserResponseDTO;
import com.example.gymrat.auth.AuthenticationResponse;
import com.example.gymrat.config.JwtService;
import com.example.gymrat.exception.auth.InvalidCredentialsException;
import com.example.gymrat.exception.user.UserAlreadyExistsException;
import com.example.gymrat.exception.user.UserNotFoundException;
import com.example.gymrat.mapper.UserMapper;
import com.example.gymrat.model.PersonalInfo;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.PersonalInfoRepository;
import com.example.gymrat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PersonalInfoRepository personalInfoRepository;



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

        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setUser(user);
        personalInfo.setDateOfBirth(request.birthday());
        personalInfoRepository.save(personalInfo);



        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new UserNotFoundException("User not found"));
        String jwtToken = jwtService.generateToken(user);

        return  AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean isCurrentUser(Long userId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);
        return currentUser != null && currentUser.getId().equals(userId);
    }

    public UserResponseDTO getUserInfo() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow();

        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public User getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() ->  new UserNotFoundException("User not found with email: " + userEmail));
    }

    public void setCurrentUser(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities())
        );
    }




}
