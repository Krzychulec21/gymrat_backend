package com.example.gymrat.DTO.friends;

import java.time.LocalDateTime;

public record FriendResponseDTO(

        Long id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime latestMessageTimestamp
) {
}
