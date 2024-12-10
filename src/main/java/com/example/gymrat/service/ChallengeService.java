package com.example.gymrat.service;

import com.example.gymrat.DTO.challenge.ChallengeRequestDTO;
import com.example.gymrat.DTO.challenge.ChallengeResponseDTO;
import com.example.gymrat.DTO.challenge.MedalInfoDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.mapper.ChallengeMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.ChallengeParticipantRepository;
import com.example.gymrat.repository.ChallengeRepository;
import com.example.gymrat.repository.ChallengeTypeRepository;
import com.example.gymrat.specification.ChallengeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeTypeRepository challengeTypeRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final ChallengeMapper challengeMapper;
    private final UserService userService;


    public ChallengeResponseDTO saveChallenge(ChallengeRequestDTO dto) {
        User currentUser = userService.getCurrentUser();
        ChallengeType challengeType = getChallengeTypeById(dto.typeId());

        Challenge challenge = new Challenge();
        challenge.setName(dto.name());
        challenge.setChallengeType(challengeType);
        challenge.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge.setEndDate(dto.endDate());
        challenge.setPublic(dto.isPublic());

        ChallengeParticipant challengeParticipant = new ChallengeParticipant();
        challengeParticipant.setChallenge(challenge);
        challengeParticipant.setUser(currentUser);
        challengeParticipant.setScore(0.0);
        challengeParticipant.setLastUpdated(LocalDateTime.now());

        challenge.getChallengeParticipants().add(challengeParticipant);

        challengeRepository.save(challenge);

        return challengeMapper.mapToDto(challenge);
    }

    private ChallengeType getChallengeTypeById(Long id) {
        return challengeTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge type with given ID does not exist!"));
    }


    public Page<ChallengeResponseDTO> getActivePublicChallenges(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.Direction.fromString(sortDir),
                sortBy.equals("challengeType") ? "challengeType.name" : sortBy
        );
        Specification<Challenge> specification = Specification
                .where(ChallengeSpecification.isPublic())
                .and(ChallengeSpecification.isActive())
                .and(ChallengeSpecification.hasValidStartDate())
                .and(ChallengeSpecification.hasValidEndDate());

        return challengeRepository.findAll(specification, pageable).map(challengeMapper::mapToDto);
    }

    public Page<ChallengeResponseDTO> getActivePrivateChallenges(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.Direction.fromString(sortDir),
                sortBy.equals("challengeType") ? "challengeType.name" : sortBy
        );
        Specification<Challenge> specification = Specification
                .where(ChallengeSpecification.isPrivate())
                .and(ChallengeSpecification.isActive())
                .and(ChallengeSpecification.hasValidStartDate())
                .and(ChallengeSpecification.hasValidEndDate());

        return challengeRepository.findAll(specification, pageable).map(challengeMapper::mapToDto);
    }

    public void joinChallenge(Long id) {
        User currentUser = userService.getCurrentUser();
        Challenge challenge = getChallengeById(id);

        boolean alreadyJoined = challengeParticipantRepository.findByUserIdAndChallengeId(currentUser.getId(), challenge.getId()).isPresent();

        if (alreadyJoined) {
            throw new IllegalArgumentException("User already joined to this challenge");
        }

        ChallengeParticipant challengeParticipant = new ChallengeParticipant();
        challengeParticipant.setScore(0.0);
        challengeParticipant.setLastUpdated(LocalDateTime.now());
        challengeParticipant.setUser(currentUser);
        challengeParticipant.setChallenge(challenge);

        challengeParticipantRepository.save(challengeParticipant);
    }

    private Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge with given ID doesnt exist"));
    }

    //1
    public MedalInfoDTO getUserMedalsForFinishedPublicChallenges() {
        User currentUser = userService.getCurrentUser();
        Specification<Challenge> spec = Specification
                .where(ChallengeSpecification.isPublic())
                .and(ChallengeSpecification.isFinished())
                .and(ChallengeSpecification.belongsToUser(currentUser.getId()));

        List<Challenge> finishedPublicChallenges = challengeRepository.findAll(spec);

        int gold = 0;
        int silver = 0;
        int bronze = 0;

        for (Challenge c : finishedPublicChallenges) {
            List<ChallengeParticipant> sortedParticipants = c.getChallengeParticipants().stream()
                    .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                    .limit(3)
                    .toList();

            for (int i = 0; i < sortedParticipants.size(); i++) {
                ChallengeParticipant cp = sortedParticipants.get(i);

                if (cp.getUser().getId().equals(currentUser.getId()) && cp.getScore() > 0) {
                    if (i == 0) gold++;
                    else if (i == 1) silver++;
                    else if (i == 2) bronze++;

                    break;
                }
            }
        }

        return new MedalInfoDTO(gold, silver, bronze);
    }
}


// id1: maksymalne wyciscienie na raz
// id 2 : nadluzsza passa treningu
