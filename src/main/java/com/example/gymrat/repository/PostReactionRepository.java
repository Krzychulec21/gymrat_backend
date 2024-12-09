package com.example.gymrat.repository;

import com.example.gymrat.model.Post;
import com.example.gymrat.model.PostReaction;
import com.example.gymrat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    int countByPost(Post post);

    boolean existsByPostAndUser(Post post, User user);

    Optional<PostReaction> findByPostAndUser(Post post, User user);
}