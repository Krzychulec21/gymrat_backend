package com.example.gymrat.service;

import com.example.gymrat.DTO.chat.ChatMessageDTO;
import com.example.gymrat.exception.chat.ChatRoomNotFoundException;
import com.example.gymrat.exception.user.UserNotFoundException;
import com.example.gymrat.mapper.MessageMapper;
import com.example.gymrat.model.ChatRoom;
import com.example.gymrat.model.Message;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.ChatRoomRepository;
import com.example.gymrat.repository.MessageRepository;
import com.example.gymrat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.MessagingAdviceBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public Message saveMessage(ChatMessageDTO chatMessageDTO) {
        User sender = userRepository.findById(chatMessageDTO.senderId()).orElseThrow(
                () -> new UserNotFoundException("Sender not found with ID: " + chatMessageDTO.senderId()));

        User receiver = userRepository.findById(chatMessageDTO.receiverId()).orElseThrow(
                () -> new UserNotFoundException("Receiver not found with ID: " + chatMessageDTO.receiverId()));

        ChatRoom chatRoom = chatRoomRepository.findByUsers(chatMessageDTO.senderId(), chatMessageDTO.receiverId()).orElseGet(
                () -> {
                    ChatRoom newChatRoom = new ChatRoom();
                    newChatRoom.setUser1(sender);
                    newChatRoom.setUser2(receiver);
                    return chatRoomRepository.save(newChatRoom);
                });

        Message message = new Message();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(chatMessageDTO.content());
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }
    public Optional<Long> getChatRoomId(Long senderId, Long receiverId) {
        return chatRoomRepository.findByUsers(senderId, receiverId).map(ChatRoom::getId);
    }

    public ChatRoom createChatRoom(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow(
                () -> new UserNotFoundException("Sender not found with ID: " + senderId));

        User receiver = userRepository.findById(receiverId).orElseThrow(
                () -> new UserNotFoundException("Receiver not found with ID: " + receiverId));
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUser1(sender);
        chatRoom.setUser2(receiver);
        chatRoomRepository.save(chatRoom);
        return chatRoom;

    }
    public void sendMessage(ChatMessageDTO chatMessageDTO) {
        Message savedMessage = saveMessage(chatMessageDTO);
        messagingTemplate.convertAndSend("/topic/chat/" + chatMessageDTO.chatRoomId(), messageMapper.toDto(savedMessage));
    }

    public List<ChatMessageDTO> getChatHistory(Long roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            throw new ChatRoomNotFoundException("Chat room with ID: " + roomId + "does not exists!");
        }

        return messageRepository.findByChatRoomId(roomId)
                .stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }
}
