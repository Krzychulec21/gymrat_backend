package com.example.gymrat.controller;

import com.example.gymrat.DTO.chat.ChatMessageDTO;
import com.example.gymrat.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageDTO chatMessageDTO) {
        chatService.sendMessage(chatMessageDTO);
    }

    @GetMapping("/chat/history/{roomId}")
    @PreAuthorize("@chatService.isParticipantInChatRoom(#roomId)")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getChatHistory(roomId));
    }


    @GetMapping("/chat/room")
    @PreAuthorize("@userService.isCurrentUser(#senderId)")
    public ResponseEntity<Long> getChatRoom(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        return ResponseEntity.ok(chatService.getChatRoomId(senderId, receiverId));
    }
}
