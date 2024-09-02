package com.example.gymrat.service;

import com.example.gymrat.config.JwtService;
import com.example.gymrat.mapper.CustomUserDetails;
import com.example.gymrat.model.AuthProvider;
import com.example.gymrat.model.Role;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public void handleOAuth2LoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            newUser.setRole(Role.USER);
            userRepository.save(newUser);
            return newUser;
        });

        CustomUserDetails userDetails = new CustomUserDetails(oAuth2User, user);
        String jwtToken = jwtService.generateToken(userDetails);

        response.sendRedirect("http://localhost:3000/oauth2/redirect?token=" + jwtToken);
    }
}
