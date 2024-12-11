package com.example.gymrat.service;

import com.example.gymrat.DTO.challenge.*;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.mapper.ChallengeMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.*;
import com.example.gymrat.specification.ChallengeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeTypeRepository challengeTypeRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final ChallengeMapper challengeMapper;
    private final UserService userService;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;


    public ChallengeResponseDTO saveChallenge(ChallengeRequestDTO dto) {
        User currentUser = userService.getCurrentUser();
        ChallengeType challengeType = getChallengeTypeById(dto.typeId());
        System.out.println("weszlo w save challenge" + dto.exerciseId());
        Challenge challenge = new Challenge();
        challenge.setName(dto.name());
        challenge.setChallengeType(challengeType);
        challenge.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge.setEndDate(dto.endDate());
        challenge.setPublic(dto.isPublic());
        challenge.setAuthor(currentUser);
        if (challengeType.getId() != 1) {
            Exercise exercise = getExerciseById(dto.exerciseId());
            challenge.setExercise(exercise);
        }

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

    private Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise with given ID does not exist"));
    }

    //1 - medals
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

    private MedalInfoDTO calculateUserMedals(User user) {
        Specification<Challenge> spec = Specification
                .where(ChallengeSpecification.isPublic())
                .and(ChallengeSpecification.isFinished());

        List<Challenge> finishedPublicChallenges = challengeRepository.findAll(spec);

        int gold = 0;
        int silver = 0;
        int bronze = 0;

        for (Challenge c : finishedPublicChallenges) {
            List<ChallengeParticipant> sortedParticipants = c.getChallengeParticipants().stream()
                    .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                    .toList();

            for (int i = 0; i < sortedParticipants.size(); i++) {
                ChallengeParticipant cp = sortedParticipants.get(i);
                if (cp.getUser().getId().equals(user.getId()) && cp.getScore() > 0) {
                    if (i == 0) gold++;
                    else if (i == 1) silver++;
                    else if (i == 2) bronze++;
                }
            }
        }
        return new MedalInfoDTO(gold, silver, bronze);
    }

    //2 - all users ranking
    public Page<UserRankingDTO> getAllUsersRanking(int page, int size, String sortBy, String sortDir) {
        List<User> allUsers = userRepository.findAll();

        List<UserRankingDTO> rankings = createUserRankingList(allUsers);


        Comparator<UserRankingDTO> comparator;
        switch (sortBy) {
            case "nickname" -> comparator = Comparator.comparing(UserRankingDTO::nickname);
            case "goldMedals" -> comparator = Comparator.comparingInt(UserRankingDTO::goldMedals);
            case "silverMedals" -> comparator = Comparator.comparingInt(UserRankingDTO::silverMedals);
            case "bronzeMedals" -> comparator = Comparator.comparingInt(UserRankingDTO::bronzeMedals);
            case "totalPoints" -> comparator = Comparator.comparingInt(UserRankingDTO::totalPoints);
            default -> comparator = Comparator.comparingInt(UserRankingDTO::totalPoints).reversed();
        }
        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        rankings.sort(comparator);

        for (int i = 0; i < rankings.size(); i++) {
            UserRankingDTO r = rankings.get(i);
            rankings.set(i, new UserRankingDTO(r.userId(), r.nickname(), r.goldMedals(), r.silverMedals(), r.bronzeMedals(), r.totalPoints(), i + 1));
        }


        int start = page * size;
        int end = Math.min(start + size, rankings.size());
        List<UserRankingDTO> pageContent = rankings.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, size), rankings.size());
    }

    private List<UserRankingDTO> createUserRankingList(List<User> users) {
        List<UserRankingDTO> list = new ArrayList<>();
        for (User u : users) {
            MedalInfoDTO medals = calculateUserMedals(u);
            int totalPoints = medals.goldMedals() * 3 + medals.silverMedals() * 2 + medals.bronzeMedals();
            list.add(challengeMapper.mapToUserRankingDTO(u.getId(), u.getNickname(), medals.goldMedals(), medals.silverMedals(), medals.bronzeMedals(), totalPoints, 0));
        }
        return list;
    }

    //3 - user friends ranking
    public Page<UserRankingDTO> getFriendsAndUserRanking(int page, int size, String sortBy, String sortDir) {
        User currentUser = userService.getCurrentUser();
        Set<User> friends = currentUser.getFriends();
        Set<User> group = new HashSet<>(friends);
        group.add(currentUser);

        List<UserRankingDTO> rankings = createUserRankingList(new ArrayList<>(group));

        Comparator<UserRankingDTO> comparator;
        switch (sortBy) {
            case "nickname" -> comparator = Comparator.comparing(UserRankingDTO::nickname);
            case "goldMedals" -> comparator = Comparator.comparingInt(UserRankingDTO::goldMedals);
            case "silverMedals" -> comparator = Comparator.comparingInt(UserRankingDTO::silverMedals);
            case "bronzeMedals" -> comparator = Comparator.comparingInt(UserRankingDTO::bronzeMedals);
            case "totalPoints" -> comparator = Comparator.comparingInt(UserRankingDTO::totalPoints);
            default -> comparator = Comparator.comparingInt(UserRankingDTO::totalPoints).reversed();
        }
        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        rankings.sort(comparator);

        for (int i = 0; i < rankings.size(); i++) {
            UserRankingDTO r = rankings.get(i);
            rankings.set(i, new UserRankingDTO(r.userId(), r.nickname(), r.goldMedals(), r.silverMedals(), r.bronzeMedals(), r.totalPoints(), i + 1));
        }

        int start = page * size;
        int end = Math.min(start + size, rankings.size());
        List<UserRankingDTO> pageContent = rankings.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, size), rankings.size());
    }


    //4 - active user challenges
    public Page<ActiveChallengeDTO> getActiveChallengesForUser(int page, int size, String sortBy, String sortDir) {
        User currentUser = userService.getCurrentUser();
        Pageable pageable = createPageRequest(page, size, sortBy, sortDir);

        Specification<Challenge> spec = Specification
                .where(ChallengeSpecification.belongsToUser(currentUser.getId()))
                .and(ChallengeSpecification.isActive())
                .and(ChallengeSpecification.hasValidStartDate())
                .and(ChallengeSpecification.hasValidEndDate());

        Page<Challenge> result = challengeRepository.findAll(spec, pageable);
        return result.map(challengeMapper::mapToActiveChallengeDTO);
    }

    private Pageable createPageRequest(int page, int size, String sortBy, String sortDir) {
        return PageRequest.of(
                page,
                size,
                Sort.Direction.fromString(sortDir),
                sortBy.equals("challengeType") ? "challengeType.name" : sortBy
        );
    }

    //5 all active chellenges which user not beloing
    public Page<AvailableChallengeDTO> getAvailableActiveChallengesForUser(int page, int size, String sortBy, String sortDir,
                                                                           boolean publicFilter,
                                                                           String typeFilter,
                                                                           String categoryFilter) {
        User currentUser = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.Direction.fromString(sortDir),
                sortBy
        );
        Specification<Challenge> spec = Specification
                .where(ChallengeSpecification.notBelongsToUser(currentUser.getId()))
                .and(ChallengeSpecification.isActive())
                .and(ChallengeSpecification.hasValidStartDate())
                .and(ChallengeSpecification.hasValidEndDate())
                .and(ChallengeSpecification.filterPublic(publicFilter))
                .and(ChallengeSpecification.typeEquals(typeFilter))
                .and(ChallengeSpecification.exerciseCategoryEquals(categoryFilter));

        Page<Challenge> result = challengeRepository.findAll(spec, pageable);
        return result.map(challengeMapper::mapToAvailableChallengeDTO);
    }


    public Page<ChallengeHistoryDTO> getUserChallengeHistory(int page, int size, String sortBy, String sortDir) {
        User currentUser = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Specification<Challenge> spec = Specification
                .where(ChallengeSpecification.belongsToUser(currentUser.getId()))
                .and(ChallengeSpecification.isFinished());

        Page<Challenge> finishedChallenges = challengeRepository.findAll(spec, pageable);
        List<ChallengeHistoryDTO> history = finishedChallenges.stream().map(c -> {
            Optional<ChallengeParticipant> cp = c.getChallengeParticipants().stream().filter(p -> p.getUser().getId().equals(currentUser.getId())).findFirst();
            boolean userHasScore = cp.map(part -> part.getScore() > 0).orElse(false);
            return new ChallengeHistoryDTO(
                    c.getId(),
                    c.getName(),
                    c.getChallengeType().getName(),
                    c.getChallengeStatus().name(),
                    c.getStartDate(),
                    c.getEndDate(),
                    c.isPublic(),
                    c.getChallengeParticipants().size(),
                    c.getExercise() != null ? c.getExercise().getName() : null,
                    userHasScore
            );
        }).toList();

        return new PageImpl<>(history, pageable, finishedChallenges.getTotalElements());
    }

    public ChallengeDetailsDTO getChallengeDetails(Long id) {
        User currentUser = userService.getCurrentUser();
        Challenge challenge = getChallengeById(id);
        ChallengeResponseDTO cDto = challengeMapper.mapToDto(challenge);
        List<ChallengeParticipantResponseDTO> participants = challenge.getChallengeParticipants().stream()
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .map(cp -> new ChallengeParticipantResponseDTO(
                        cp.getId(),
                        cp.getUser().getNickname(),
                        cp.getScore(),
                        cp.getLastUpdated()
                ))
                .toList();
        return new ChallengeDetailsDTO(cDto, participants, Objects.equals(currentUser.getId(), challenge.getAuthor().getId()));
    }

    public void deleteChallenge(Long id) {
        User currentUser = userService.getCurrentUser();
        Challenge challenge = getChallengeById(id);

        if (!challenge.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this challenge");
        }

        challengeRepository.delete(challenge);
    }

    public Page<ChallengeParticipantResponseDTO> getChallengeParticipants(Long challengeId, int page, int size, String sortBy, String sortDir) {
        Challenge challenge = getChallengeById(challengeId);

        List<ChallengeParticipant> participants = new ArrayList<>(challenge.getChallengeParticipants().stream().toList());

        Comparator<ChallengeParticipant> comparator;
        if (sortBy.equals("score")) {
            comparator = Comparator.comparingDouble(ChallengeParticipant::getScore);
            if ("desc".equalsIgnoreCase(sortDir)) {
                comparator = comparator.reversed();
            }
        } else {
            comparator = Comparator.comparing(cp -> cp.getUser().getNickname());
            if ("desc".equalsIgnoreCase(sortDir)) {
                comparator = comparator.reversed();
            }
        }
        participants.sort(comparator);

        int start = page * size;
        int end = Math.min(start + size, participants.size());
        List<ChallengeParticipantResponseDTO> content = participants.subList(Math.min(start, participants.size()), end).stream()
                .map(cp -> new ChallengeParticipantResponseDTO(
                        cp.getId(),
                        cp.getUser().getNickname(),
                        cp.getScore(),
                        cp.getLastUpdated()
                ))
                .toList();

        return new PageImpl<>(content, PageRequest.of(page, size), participants.size());
    }

    public List<ChallengeTypeDTO> getAllChallengeTypes() {
        return challengeTypeRepository.findAll()
                .stream().map(challengeType -> new ChallengeTypeDTO(
                        challengeType.getId(),
                        challengeType.getName(),
                        challengeType.getDescription()
                )).toList();
    }


}

