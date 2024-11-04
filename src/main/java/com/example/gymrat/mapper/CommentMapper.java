package com.example.gymrat.mapper;

import com.example.gymrat.DTO.trainingPlan.CommentResponseDTO;
import com.example.gymrat.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentResponseDTO mapToDTO(Comment comment) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getNickname(),
                comment.getDateCreated()
        );
    }
}
