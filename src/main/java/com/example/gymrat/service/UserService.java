package com.example.gymrat.service;

import com.example.gymrat.DTO.admin.WarnMessageDTO;
import com.example.gymrat.DTO.auth.AuthenticationRequest;
import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.DTO.user.UserResponseDTO;
import com.example.gymrat.RabbitMQ.EmailProducer;
import com.example.gymrat.config.AuthenticationResponse;
import com.example.gymrat.config.JwtService;
import com.example.gymrat.exception.auth.EmailNotVerifiedException;
import com.example.gymrat.exception.auth.InvalidCredentialsException;
import com.example.gymrat.exception.auth.UserBlockedException;
import com.example.gymrat.exception.user.UserAlreadyExistsException;
import com.example.gymrat.exception.user.UserNotFoundException;
import com.example.gymrat.mapper.UserMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.PersonalInfoRepository;
import com.example.gymrat.repository.ResetPasswordTokenRepository;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PersonalInfoRepository personalInfoRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailProducer emailProducer;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final NotificationService notificationService;


    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email is taken", "EMAIL_TAKEN");
        }
        if (userRepository.findByNickname(request.nickname()).isPresent()) {
            throw new UserAlreadyExistsException("Nickname is taken", "NICKNAME_TAKEN");
        }

        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setUser(user);
        personalInfo.setDateOfBirth(request.birthday());
        personalInfoRepository.save(personalInfo);

        String token = generateVerificationToken(user);
        String verificationUrl = "http://localhost:8080/api/v1/auth/verify-email?token=" + token;

        //emailProducer.sendVerificationEmail(user.getEmail(), verificationUrl);

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

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException("Email not verified");
        }

        if (user.isBlocked()) {
            throw new UserBlockedException("User has been blocked");
        }

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public Page<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String sortDir) {
        User currentUser = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<User> userPage = userRepository.findAllExceptCurrentUser(currentUser.getId(), pageable);

        return userPage.map(user -> new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getNickname(),
                user.getEmail()
        ));
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
                user.getNickname(),
                user.getEmail()
        );
    }

    public UserResponseDTO getUserInfo(Long userId) {
        User user = getUserById(userId);

        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getNickname(),
                user.getEmail()
        );
    }


    public void sendPasswordResetLink(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email not registered"));

        String token = generateResetToken(user);
        String resetUrl = "http://localhost:3000/auth?token=" + token;

        emailProducer.sendResetPasswordEmail(user.getEmail(), resetUrl);
    }

    public User getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with given ID does not exist"));
    }

    public void setCurrentUser(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities())
        );
    }

    private String generateResetToken(User user) {
        String token = UUID.randomUUID().toString();
        ResetPasswordToken resetToken = new ResetPasswordToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        resetPasswordTokenRepository.save(resetToken);
        return token;
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(verificationToken);
        return token;
    }


    public void updatePassword(String token, String newPassword) {
        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetPasswordToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        User user = resetPasswordToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetPasswordTokenRepository.delete(resetPasswordToken);
    }

    public void sendWarnToUser(Long userId, WarnMessageDTO dto) {
        User user = getUserById(userId);
        System.out.println("otrzymane descriptin" + dto);
        notificationService.sendNotification(user, null, dto.description(), NotificationType.WARN, null);
    }

    public void blockUser(Long userId) {
        User user = getUserById(userId);
        user.setBlocked(true);
        userRepository.save(user);
    }

}
