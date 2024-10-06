package com.example.gymrat.DTO.friends;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for responding to a friend request")
public record FriendRequestActionDTO(
        @Schema(description = "Unique identifier of the friend request", example = "1")
        Long requestId,

        @Schema(description = "Indicates if the request is accepted", example = "true")
        boolean accepted
) {}
