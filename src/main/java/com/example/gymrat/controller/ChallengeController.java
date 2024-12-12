package com.example.gymrat.controller;

import com.example.gymrat.DTO.challenge.*;
import com.example.gymrat.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @PostMapping
    public ResponseEntity<ChallengeResponseDTO> createChallenge(@RequestBody ChallengeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(challengeService.saveChallenge(dto));
    }

    @GetMapping("/public")
    public ResponseEntity<Page<ChallengeResponseDTO>> getAllActivePublicChallenges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        return ResponseEntity.ok(challengeService.getActivePublicChallenges(page, size, sortBy, sortDir));
    }

    @GetMapping("/private")
    public ResponseEntity<Page<ChallengeResponseDTO>> getAllActivePrivateChallenges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        return ResponseEntity.ok(challengeService.getActivePrivateChallenges(page, size, sortBy, sortDir));
    }

    @PostMapping("/{id}/participate")
    public ResponseEntity<Void> joinChallenge(@PathVariable Long id) {
        challengeService.joinChallenge(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/user/medals")
    public ResponseEntity<MedalInfoDTO> getUserMedals() {
        return ResponseEntity.ok(challengeService.getUserMedalsForFinishedPublicChallenges());
    }

    @GetMapping("/ranking/all")
    public ResponseEntity<Page<UserRankingDTO>> getAllUsersRanking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "totalPoints") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(challengeService.getAllUsersRanking(page, size, sortBy, sortDir));
    }

    @GetMapping("/ranking/friends")
    public ResponseEntity<Page<UserRankingDTO>> getFriendsRanking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "totalPoints") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(challengeService.getFriendsAndUserRanking(page, size, sortBy, sortDir));
    }

    @GetMapping("/user/active")
    public ResponseEntity<Page<ActiveChallengeDTO>> getUserActiveChallenges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(challengeService.getActiveChallengesForUser(page, size, sortBy, sortDir));
    }

    @GetMapping("/user/available")
    public ResponseEntity<Page<AvailableChallengeDTO>> getAvailableActiveChallengesForUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Boolean publicFilter,
            @RequestParam(required = false) String typeFilter,
            @RequestParam(required = false) String categoryFilter
    ) {
        return ResponseEntity.ok(challengeService.getAvailableActiveChallengesForUser(page, size, sortBy, sortDir, publicFilter, typeFilter, categoryFilter));
    }

    @GetMapping("/user/history")
    public ResponseEntity<Page<ChallengeHistoryDTO>> getUserChallengeHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(challengeService.getUserChallengeHistory(page, size, sortBy, sortDir));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ChallengeDetailsDTO> getChallengeDetails(@PathVariable Long id) {
        return ResponseEntity.ok(challengeService.getChallengeDetails(id));
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<Page<ChallengeParticipantResponseDTO>> getChallengeParticipants(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "score") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(challengeService.getChallengeParticipants(id, page, size, sortBy, sortDir));
    }

    @GetMapping("/types")
    public ResponseEntity<List<ChallengeTypeDTO>> getAllChallengeTypes() {
        return ResponseEntity.ok(challengeService.getAllChallengeTypes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {
        challengeService.deleteChallenge(id);
        return ResponseEntity.noContent().build();
    }


}
