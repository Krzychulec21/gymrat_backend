package com.example.gymrat.mapper;

import com.example.gymrat.DTO.personalInfo.PersonalInfoRequestDTO;
import com.example.gymrat.DTO.personalInfo.PersonalInfoResponseDTO;
import com.example.gymrat.model.PersonalInfo;
import org.springframework.stereotype.Component;

@Component
public class PersonalInfoMapper {
    public PersonalInfoResponseDTO toDTO(PersonalInfo personalInfo) {
        return new PersonalInfoResponseDTO(
                personalInfo.getId(),
                personalInfo.getDateOfBirth(),
                personalInfo.getHeight(),
                personalInfo.getWeight(),
                personalInfo.getGender(),
                personalInfo.getBio()
        );
    }

    public void updatePersonalInfoFromDTO(PersonalInfoRequestDTO requestDTO, PersonalInfo personalInfo) {
        personalInfo.setDateOfBirth(requestDTO.dateOfBirth());
        personalInfo.setWeight(requestDTO.weight());
        personalInfo.setHeight(requestDTO.height());
        personalInfo.setGender(requestDTO.gender());
        personalInfo.setBio(requestDTO.bio());
    }
}
