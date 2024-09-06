package com.example.gymrat.DTO.user;

public record UserWithRequestStatusDTO(
         Long id,
         String firstName,
         String lastName,
         String email,
         boolean isFriendRequestSent
) {}
