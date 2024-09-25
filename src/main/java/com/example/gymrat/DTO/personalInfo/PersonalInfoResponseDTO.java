package com.example.gymrat.DTO.personalInfo;

import com.example.gymrat.model.Gender;

import java.time.LocalDate;

public record PersonalInfoResponseDTO (
        Long id,
        LocalDate dateOfBirth,
        Double height,
        Double weight,
        Gender gender,
        String bio
) {
}
