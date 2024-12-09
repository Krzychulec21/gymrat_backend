package com.example.gymrat.controller;

import com.example.gymrat.DTO.post.PostResponseDTO;
import com.example.gymrat.config.AppProperties;
import com.example.gymrat.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final AppProperties appProperties;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createPost(
            @RequestPart("workoutId") String workoutId,
            @RequestPart("description") String description,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        Long workoutIdLong = Long.parseLong(workoutId);
        postService.createPost(workoutIdLong, description, photo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getUserPosts() {
        List<PostResponseDTO> posts = postService.getUserPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{postId}/reactions")
    public ResponseEntity<Void> addReaction(@PathVariable Long postId) {
        postService.addReaction(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/reactions")
    public ResponseEntity<Void> removeReaction(@PathVariable Long postId) {
        postService.removeReaction(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            String uploadDir = appProperties.getDir();
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(filename);

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                throw new RuntimeException("File does not exist or is not readable: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error while retrieving file: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Error while detecting MIME type: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDTO>> getUserPosts(@PathVariable Long userId) {
        List<PostResponseDTO> posts = postService.getUserPosts(userId);
        return ResponseEntity.ok(posts);
    }
}