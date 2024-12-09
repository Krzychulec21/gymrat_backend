package com.example.gymrat.repository;

import com.example.gymrat.model.Post;
import com.example.gymrat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    Optional<Post> findByWorkoutSessionId(Long workoutId);

}
