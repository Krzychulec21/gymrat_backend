package com.example.gymrat.DTO.personalInfo;

import com.example.gymrat.model.Gender;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PersonalInfoRequestDTO(
        @Past
        LocalDate dateOfBirth,
        @DecimalMax(value = "400.00")
        Double weight,
        @DecimalMax(value = "300.00")
        Double height,
        Gender gender,
        @Size(max = 500, message = "BIO must exceed 500 characters")
        String bio

) {
}
