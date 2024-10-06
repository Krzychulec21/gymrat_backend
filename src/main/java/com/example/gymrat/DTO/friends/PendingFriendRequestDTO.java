package com.example.gymrat.DTO.friends;

import com.example.gymrat.model.RequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for pending friend requests")
public record PendingFriendRequestDTO(
        @Schema(description = "Unique identifier of the friend request", example = "1")
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
