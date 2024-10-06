package com.example.gymrat.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object representing a user with friend request status")
public record UserWithRequestStatusDTO(
        @Schema(description = "Unique identifier of the user", example = "1")
        Long id,

        @Schema(description = "First name of the user", example = "Jane")
        String firstName,

        @Schema(description = "Last name of the user", example = "Smith")
        String lastName,

        @Schema(description = "Email address of the user", example = "jane.smith@example.com")
        String email,

        @Schema(description = "Indicates if a friend request has been sent", example = "true")
        boolean isFriendRequestSent
) {}
