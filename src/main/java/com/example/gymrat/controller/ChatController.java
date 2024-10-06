package com.example.gymrat.controller;

import com.example.gymrat.DTO.chat.ChatMessageDTO;
import com.example.gymrat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Chat", description = "Endpoints for chat functionality")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "Send a chat message", description = "Sends a chat message to a user.")
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageDTO chatMessageDTO) {
        chatService.sendMessage(chatMessageDTO);
    }

    @Operation(summary = "Get chat history", description = "Retrieves chat history for a specific chat room.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat history retrieved successfully."),
            @ApiResponse(responseCode = "403", description = "Access denied."),
            @ApiResponse(responseCode = "404", description = "Chat room not found.")
    })
    @GetMapping("/chat/history/{roomId}")
    @PreAuthorize("@chatService.isParticipantInChatRoom(#roomId)")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getChatHistory(roomId));
    }

    @Operation(summary = "Get or create chat room", description = "Retrieves or creates a chat room ID between two users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat room ID retrieved successfully."),
            @ApiResponse(responseCode = "403", description = "Access denied.")
    })
    @GetMapping("/chat/room")
    @PreAuthorize("@userService.isCurrentUser(#senderId)")
    public ResponseEntity<Long> getChatRoom(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        return ResponseEntity.ok(chatService.getChatRoomId(senderId, receiverId));
    }
}
