package com.example.gymrat.controller;

import com.example.gymrat.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Void> createPost(
            @RequestPart("workoutId") Long workoutId,
            @RequestPart("description") String description,
            @RequestPart("photo") MultipartFile photo) {

        postService.createPost(workoutId, description, photo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
