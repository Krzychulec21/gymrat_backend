package com.example.gymrat.service;

import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.model.Post;
import com.example.gymrat.model.User;
import com.example.gymrat.model.WorkoutSession;
import com.example.gymrat.repository.PostRepository;
import com.example.gymrat.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final UserService userService;

    public void createPost(Long workoutId, String description, MultipartFile photo) {
        User user = userService.getCurrentUser();

        WorkoutSession workoutSession = workoutSessionRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session with given ID does not exist"));

        String photoPath = savePhoto(photo);

        Post post = new Post();
        post.setUser(user);
        post.setWorkoutSession(workoutSession);
        post.setDescription(description);
        post.setImagePath(photoPath);

        postRepository.save(post);
    }

    private String savePhoto(MultipartFile photo) {
        try {
            String uploadDir = "uploads/posts/";
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, photo.getBytes());

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save photo", e);
        }
    }
}
