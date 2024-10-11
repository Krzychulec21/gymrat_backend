package com.example.gymrat.DTO.friends;

import com.example.gymrat.model.RequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;


public record PendingFriendRequestDTO(

        Long Id,

        @Schema(description = "Email of the sender", example = "sender@example.com")
        String senderEmail,

        @Schema(description = "First name of the sender", example = "John")
        String senderFirstName,

        @Schema(description = "Last name of the sender", example = "Doe")
        String senderLastName,

        @Schema(description = "Email of the receiver", example = "receiver@example.com")
        String receiverEmail,

        @Schema(description = "Status of the request", example = "PENDING")
        RequestStatus status
) {}
