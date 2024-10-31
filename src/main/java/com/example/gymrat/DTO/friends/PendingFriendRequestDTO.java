package com.example.gymrat.DTO.friends;

import com.example.gymrat.model.RequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;


public record PendingFriendRequestDTO(

        Long Id,

        String senderEmail,

        String senderFirstName,

        String senderLastName,

        String receiverEmail,

        RequestStatus status
) {}
