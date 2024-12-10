package com.example.gymrat.controller;

import com.example.gymrat.DTO.challenge.ChallengeRequestDTO;
import com.example.gymrat.DTO.challenge.ChallengeResponseDTO;
import com.example.gymrat.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
