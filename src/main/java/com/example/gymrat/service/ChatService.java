package com.example.gymrat.service;

import com.example.gymrat.DTO.chat.ChatMessageDTO;
import com.example.gymrat.exception.chat.ChatRoomNotFoundException;
import com.example.gymrat.exception.user.UserNotFoundException;
import com.example.gymrat.mapper.MessageMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.ChatRoomRepository;
import com.example.gymrat.repository.MessageRepository;
import com.example.gymrat.repository.NotificationRepository;
import com.example.gymrat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.MessagingAdviceBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    private final NotificationService notificationService;

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

    public Long getChatRoomId(Long senderId, Long receiverId) {
        return chatRoomRepository.findByUsers(senderId, receiverId).orElseGet(() -> {
            User user1 = userRepository.findById(senderId).orElseThrow(() ->
                    new UserNotFoundException("User with ID " + senderId + " not found"));
            User user2 = userRepository.findById(receiverId).orElseThrow(() ->
                    new UserNotFoundException("User with ID " + receiverId + " not found"));
            ChatRoom newChatRoom = new ChatRoom();
            newChatRoom.setUser1(user1);
            newChatRoom.setUser2(user2);
            return chatRoomRepository.save(newChatRoom);
        }).getId();
    }

    public void sendMessage(ChatMessageDTO chatMessageDTO) {
        Message savedMessage = saveMessage(chatMessageDTO);


        User sender = userRepository.findById(chatMessageDTO.senderId()).orElseThrow(() ->
                new UserNotFoundException("User with ID " + chatMessageDTO.senderId() + " not found"));
        User receiver = userRepository.findById(chatMessageDTO.receiverId()).orElseThrow(() ->
                new UserNotFoundException("User with ID " + chatMessageDTO.receiverId() + " not found"));

        messagingTemplate.convertAndSendToUser(receiver.getEmail(), "/queue/chat/" + chatMessageDTO.chatRoomId(), messageMapper.toDto(savedMessage));
        messagingTemplate.convertAndSendToUser(sender.getEmail(), "/queue/chat/" + chatMessageDTO.chatRoomId(), messageMapper.toDto(savedMessage));


        notificationService.sendNotification(receiver, "Nowa wiadomość od "+ sender.getFirstName() +" " + sender.getLastName(), NotificationType.MESSAGE);
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


    //Authorization methods
    public boolean isParticipantInChatRoom(Long roomId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return false;
        }
        System.out.println("user id"+ user.getId());
        return chatRoomRepository.existsByIdAndUserId(roomId, user.getId());
    }
}
