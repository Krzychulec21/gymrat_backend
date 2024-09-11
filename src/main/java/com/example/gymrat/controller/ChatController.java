package com.example.gymrat.controller;

import com.example.gymrat.DTO.chat.ChatMessageDTO;
import com.example.gymrat.model.ChatRoom;
import com.example.gymrat.model.Message;
import com.example.gymrat.repository.MessageRepository;
import com.example.gymrat.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getChatHistory(roomId));
    }

    @GetMapping("/chat/room")
    public ResponseEntity<Long> getOrCreateChatRoom(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        Long chatRoomId = chatService.getChatRoomId(senderId, receiverId).orElseGet(() -> {
            ChatRoom newChatRoom = chatService.createChatRoom(senderId, receiverId);
            return newChatRoom.getId();
        });

        return ResponseEntity.ok(chatRoomId);
    }


}
