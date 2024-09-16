package com.example.gymrat.repository;

import com.example.gymrat.DTO.chat.ChatMessageDTO;
import com.example.gymrat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoomId(Long chatRoomId);
}
