package com.example.gymrat.service;

import com.example.gymrat.DTO.post.PostResponseDTO;
import com.example.gymrat.config.AppProperties;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.PostReactionRepository;
import com.example.gymrat.repository.PostRepository;
import com.example.gymrat.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final UserService userService;
    private final PostReactionRepository postReactionRepository;
    private final AppProperties appProperties;
    private final NotificationService notificationService;
    private final FriendService friendService;

    public void createPost(Long workoutId, String description, MultipartFile photo) {
        User user = userService.getCurrentUser();

        WorkoutSession workoutSession = workoutSessionRepository.findById(workoutId).orElseThrow(
                () -> new ResourceNotFoundException("Workout session with given ID does not exist"));

        Post post = new Post();
        post.setUser(user);
        post.setWorkoutSession(workoutSession);
        post.setDescription(description);
        post.setTimestamp(LocalDate.now());

        if (photo != null) {
            String fileName = savePhoto(photo);
            post.setImagePath("http://localhost:8080/api/v1/posts/images/" + fileName);
        }

        postRepository.save(post);
    }

    private String savePhoto(MultipartFile photo) {
        try {
            if (!Objects.requireNonNull(photo.getContentType()).startsWith("image/")) {
                throw new IllegalArgumentException("Invalid file type. Only images are allowed.");
            }

            if (photo.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("File size exceeds the limit of 5MB.");
            }

            String uploadDir = appProperties.getDir();
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(fileName);

            Files.createDirectories(uploadPath);


            try (InputStream inputStream = photo.getInputStream(); OutputStream outputStream = Files.newOutputStream(filePath)) {
                Thumbnails.of(inputStream).size(800, 800).outputFormat("jpg").outputQuality(0.8).toOutputStream(outputStream);
            }

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save photo", e);
        }
    }


    public List<PostResponseDTO> getUserPosts() {
        User user = userService.getCurrentUser();
        List<Post> posts = postRepository.findByUser(user);

        return posts.stream().map(post -> {
            int reactionCount = postReactionRepository.countByPost(post);
            boolean reactedByCurrentUser = postReactionRepository.existsByPostAndUser(post, user);
            return mapToPostResponse(post, user, reactionCount, reactedByCurrentUser);
        }).collect(Collectors.toList());
    }

    private PostResponseDTO mapToPostResponse(Post post, User user, int reactionCount, boolean reactedByCurrentUser) {
        return new PostResponseDTO(post.getId(), post.getDescription(), post.getImagePath(), post.getTimestamp(), post.getWorkoutSession().getId(), reactionCount, reactedByCurrentUser);
    }

    public void addReaction(Long postId) {
        User user = userService.getCurrentUser();
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        boolean alreadyReacted = postReactionRepository.existsByPostAndUser(post, user);
        if (alreadyReacted) {
            throw new IllegalStateException("User already reacted to this post");
        }

        PostReaction reaction = new PostReaction();
        reaction.setUser(user);
        reaction.setPost(post);

        postReactionRepository.save(reaction);

        if (!user.equals(post.getUser())) {
            notificationService.sendNotification(post.getUser(), user, "Ktoś zareagował na Twój post", NotificationType.POST, null);
        }
    }

    public void removeReaction(Long postId) {
        User user = userService.getCurrentUser();
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        PostReaction reaction = postReactionRepository.findByPostAndUser(post, user).orElseThrow(() -> new ResourceNotFoundException("Reaction not found"));

        postReactionRepository.delete(reaction);
    }

    public List<PostResponseDTO> getUserPosts(Long userId) {
        User currentUser = userService.getCurrentUser();
        User profileUser = userService.getUserById(userId);

        List<Post> posts = postRepository.findByUser(profileUser);

        return posts.stream().map(post -> {
            int reactionCount = postReactionRepository.countByPost(post);
            boolean reactedByCurrentUser = postReactionRepository.existsByPostAndUser(post, currentUser);
            return mapToPostResponse(post, currentUser, reactionCount, reactedByCurrentUser);
        }).collect(Collectors.toList());
    }


}
