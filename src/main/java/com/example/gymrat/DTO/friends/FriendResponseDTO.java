package com.example.gymrat.DTO.friends;

import java.time.LocalDateTime;

public record FriendResponseDTO(

        Long id,
        Long userId,
        String firstName,
        String lastName,
        String email,
        LocalDateTime latestMessageTimestamp,
        byte[] avatar
) {
}
