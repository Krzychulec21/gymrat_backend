package com.example.gymrat.controller;

import com.example.gymrat.DTO.personalInfo.PersonalInfoRequestDTO;
import com.example.gymrat.DTO.personalInfo.PersonalInfoResponseDTO;
import com.example.gymrat.service.PersonalInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PersonalInfoController {

    private final PersonalInfoService personalInfoService;

    @GetMapping("personal-info")
    public ResponseEntity<PersonalInfoResponseDTO> getPersonalInfo() {
        return ResponseEntity.ok(personalInfoService.getPersonalInfo());
    }
    @PatchMapping("/personal-info")
    public ResponseEntity<PersonalInfoResponseDTO> updatePersonalInfo(@RequestBody PersonalInfoRequestDTO requestDTO) {
        return ResponseEntity.ok(personalInfoService.updatePersonalInfo(requestDTO));
    }
}
