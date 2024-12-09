package com.example.gymrat.controller;

import com.example.gymrat.DTO.personalInfo.PersonalInfoRequestDTO;
import com.example.gymrat.DTO.personalInfo.PersonalInfoResponseDTO;
import com.example.gymrat.service.PersonalInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/personal-info")
public class PersonalInfoController {

    private final PersonalInfoService personalInfoService;

    @GetMapping("")
    public ResponseEntity<PersonalInfoResponseDTO> getPersonalInfo() {
        return ResponseEntity.ok(personalInfoService.getPersonalInfo());
    }

    @PatchMapping("")
    public ResponseEntity<PersonalInfoResponseDTO> updatePersonalInfo(@RequestBody PersonalInfoRequestDTO requestDTO) {
        return ResponseEntity.ok(personalInfoService.updatePersonalInfo(requestDTO));
    }

    @GetMapping("/avatar")
    public ResponseEntity<byte[]> getAvatar() {
        byte[] avatar = personalInfoService.getAvatar();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok()
                .headers(headers)
                .body(avatar);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PersonalInfoResponseDTO> getPersonalInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(personalInfoService.getPersonalInfo(userId));
    }

    @GetMapping("/{userId}/avatar")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long userId) {
        byte[] avatar = personalInfoService.getAvatar(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok()
                .headers(headers)
                .body(avatar);
    }

    @PatchMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAvatar(@RequestParam("file") MultipartFile file) {
        return personalInfoService.updateAvatar(file);
    }
}
