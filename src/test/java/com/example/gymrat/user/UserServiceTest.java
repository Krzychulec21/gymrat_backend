package com.example.gymrat.user;

import com.example.gymrat.DTO.auth.AuthenticationRequest;
import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.config.AuthenticationResponse;
import com.example.gymrat.config.JwtService;
import com.example.gymrat.exception.user.UserAlreadyExistsException;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.PersonalInfoRepository;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.repository.VerificationTokenRepository;
import com.example.gymrat.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PersonalInfoRepository personalInfoRepository;
    @InjectMocks
    private UserService userService;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Test
    public void testRegisterNewUserSuccess() {
        RegisterRequest request = new RegisterRequest("Jan", "Kowalski", "kowal", "kowalski@wp.pl", "password", LocalDate.of(2003, 3, 3));

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("hashed_password");
        when(jwtService.generateToken(any(User.class))).thenReturn("mock_jwt_token");

        AuthenticationResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("mock_jwt_token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testAuthenticateUserSuccess() {
        AuthenticationRequest request = new AuthenticationRequest("kowalski@wp.pl", "password");

        User user = new User();
        user.setEmail("kowalski@wp.pl");
        user.setEmailVerified(true);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("mock_jwt_token");

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authRequest);

        AuthenticationResponse response = userService.authenticate(request);

        assertNotNull(response);
        assertEquals("mock_jwt_token", response.getToken());
    }

    @Test
    public void testRegisterUserAlreadyExistsThrowsException() {
        RegisterRequest request = new RegisterRequest("Jan", "Kowalski", "kowal", "kowalski@wp.pl", "password", LocalDate.of(2003, 3, 3));

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.register(request);
        });

        verify(userRepository, never()).save(any(User.class));
    }
}
