package com.example.gymrat.DTO.post;

import org.springframework.web.multipart.MultipartFile;

public record CreatePostDTO(
        Long workoutId,
        String description,
        MultipartFile photo
) {
}
