package com.example.gymrat.mapper;

import com.example.gymrat.DTO.chat.ChatMessageDTO;
import com.example.gymrat.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public ChatMessageDTO toDto(Message message) {
        return new ChatMessageDTO(
                message.getId(),
                message.getContent(),
                message.getSender().getId(),
                message.getReceiver().getId(),
                message.getTimestamp(),
                message.getChatRoom().getId()
        );
    }
}
