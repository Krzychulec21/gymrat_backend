package com.example.gymrat.DTO.friends;

import com.example.gymrat.model.RequestStatus;


public record PendingFriendRequestDTO(

        Long Id,

        String senderEmail,

        String senderFirstName,

        String senderLastName,

        String receiverEmail,

        RequestStatus status
) {
}
