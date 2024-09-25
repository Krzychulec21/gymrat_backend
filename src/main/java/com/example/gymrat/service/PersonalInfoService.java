package com.example.gymrat.service;

import com.example.gymrat.DTO.personalInfo.PersonalInfoRequestDTO;
import com.example.gymrat.DTO.personalInfo.PersonalInfoResponseDTO;
import com.example.gymrat.mapper.PersonalInfoMapper;
import com.example.gymrat.model.PersonalInfo;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.PersonalInfoRepository;
import com.example.gymrat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        if (personalInfo == null) {
            personalInfo = new PersonalInfo();
            personalInfo.setUser(user);
            personalInfoRepository.save(personalInfo);
        }

        return personalInfoMapper.toDTO(personalInfo);
    }
}
