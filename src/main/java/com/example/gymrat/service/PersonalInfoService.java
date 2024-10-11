package com.example.gymrat.service;

import com.example.gymrat.DTO.personalInfo.PersonalInfoRequestDTO;
import com.example.gymrat.DTO.personalInfo.PersonalInfoResponseDTO;
import com.example.gymrat.exception.InvalidFileFormatException;
import com.example.gymrat.mapper.PersonalInfoMapper;
import com.example.gymrat.model.PersonalInfo;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.PersonalInfoRepository;
import com.example.gymrat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;

@Service
@RequiredArgsConstructor
public class PersonalInfoService {
    private final UserService userService;
    private final PersonalInfoRepository personalInfoRepository;
    private final PersonalInfoMapper personalInfoMapper;

    public PersonalInfoResponseDTO updatePersonalInfo(PersonalInfoRequestDTO requestDTO) {
        User user = userService.getCurrentUser();

        PersonalInfo personalInfo = user.getPersonalInfo();

        personalInfoMapper.updatePersonalInfoFromDTO(requestDTO, personalInfo);

        PersonalInfo savedPersonalInfo = personalInfoRepository.save(personalInfo);

        return personalInfoMapper.toDTO(savedPersonalInfo);
    }

    public PersonalInfoResponseDTO getPersonalInfo() {
        User user = userService.getCurrentUser();

        PersonalInfo personalInfo = user.getPersonalInfo();

        return personalInfoMapper.toDTO(personalInfo);
    }

    public byte[] getAvatar() {
        User user = userService.getCurrentUser();
        return user.getPersonalInfo().getAvatar();
    }

    public ResponseEntity<String> updateAvatar(MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {
            byte[] avatarData = file.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(avatarData);

            String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
            inputStream.reset();

            if (mimeType == null || !mimeType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Unsupported file format");
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(inputStream)
                    .size(360, 360)
                    .outputFormat("jpeg")
                    .toOutputStream(outputStream);

            User user = userService.getCurrentUser();
            PersonalInfo personalInfo = user.getPersonalInfo();
            personalInfo.setAvatar(outputStream.toByteArray());
            personalInfoRepository.save(personalInfo);

            return ResponseEntity.ok("Avatar updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process avatar image");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

}
