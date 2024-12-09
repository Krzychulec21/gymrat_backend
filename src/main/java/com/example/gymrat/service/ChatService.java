package com.example.gymrat.service;

import com.example.gymrat.DTO.chat.ChatMessageDTO;
import com.example.gymrat.exception.chat.ChatRoomNotFoundException;
import com.example.gymrat.exception.user.UserNotFoundException;
import com.example.gymrat.mapper.MessageMapper;
import com.example.gymrat.model.ChatRoom;
import com.example.gymrat.model.Message;
import com.example.gymrat.model.NotificationType;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.ChatRoomRepository;
import com.example.gymrat.repository.MessageRepository;
import com.example.gymrat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public Message saveMessage(ChatMessageDTO chatMessageDTO) {
        User sender = getUserById(chatMessageDTO.senderId());
        User receiver = getUserById(chatMessageDTO.receiverId());

        ChatRoom chatRoom = chatRoomRepository.findByUsers(chatMessageDTO.senderId(), chatMessageDTO.receiverId()).orElseGet(
                () -> createNewChatRoom(sender, receiver));

        Message message = new Message();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(chatMessageDTO.content());
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public Long getChatRoomId(Long senderId, Long receiverId) {
        return chatRoomRepository.findByUsers(senderId, receiverId)
                .map(ChatRoom::getId)
                .orElseGet(() -> createNewChatRoom(getUserById(senderId), getUserById(receiverId)).getId());
    }

    public void sendMessage(ChatMessageDTO chatMessageDTO) {
        Message savedMessage = saveMessage(chatMessageDTO);
        User sender = getUserById(chatMessageDTO.senderId());
        User receiver = getUserById(chatMessageDTO.receiverId());

        sendMessageToUsers(savedMessage, sender, receiver);
        sendNotification(sender, receiver);
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


    public boolean isParticipantInChatRoom(Long roomId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userEmail)
                .map(user -> chatRoomRepository.existsByIdAndUserId(roomId, user.getId()))
                .orElse(false);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    private ChatRoom createNewChatRoom(User user1, User user2) {
        ChatRoom newChatRoom = new ChatRoom();
        newChatRoom.setUser1(user1);
        newChatRoom.setUser2(user2);

        return chatRoomRepository.save(newChatRoom);
    }

    private void sendNotification(User sender, User receiver) {
        String notificationContent = String.format("Nowa wiadomość od %s %s", sender.getFirstName(), sender.getLastName());
        notificationService.sendNotification(receiver, sender, notificationContent, NotificationType.MESSAGE, null);
    }

    private void sendMessageToUsers(Message message, User sender, User receiver) {
        ChatMessageDTO messageDTO = messageMapper.toDto(message);
        messagingTemplate.convertAndSendToUser(receiver.getEmail(), "/queue/chat/" + message.getChatRoom().getId(), messageDTO);
        messagingTemplate.convertAndSendToUser(sender.getEmail(), "/queue/chat/" + message.getChatRoom().getId(), messageDTO);
    }

}
