package com.example.gymrat.service;

import com.example.gymrat.config.JwtService;
import com.example.gymrat.mapper.CustomUserDetails;
import com.example.gymrat.model.AuthProvider;
import com.example.gymrat.model.PersonalInfo;
import com.example.gymrat.model.Role;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.PersonalInfoRepository;
import com.example.gymrat.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PersonalInfoRepository personalInfoRepository;

    public void handleOAuth2LoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String pictrureUrl = oAuth2User.getAttribute("picture");

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

        byte[] avatarBytes = downloadAvatarAsBytes(pictrureUrl);

        PersonalInfo personalInfo = user.getPersonalInfo();
        if (personalInfo == null) {
            personalInfo = new PersonalInfo();
            personalInfo.setUser(user);
        }
        System.out.println("po");
        personalInfo.setAvatar(avatarBytes);
        personalInfoRepository.save(personalInfo);


        CustomUserDetails userDetails = new CustomUserDetails(oAuth2User, user);
        String jwtToken = jwtService.generateToken(userDetails);

        response.sendRedirect("http://localhost:3000/oauth2/redirect?token=" + jwtToken);
    }

    public byte[] downloadAvatarAsBytes(String imageUrl) throws IOException {
        if (StringUtils.hasText(imageUrl)) {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // Some servers require a user-agent

            try (InputStream in = connection.getInputStream();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                return baos.toByteArray();
            }
        }
        return null;
    }
}
