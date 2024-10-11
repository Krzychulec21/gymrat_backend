package com.example.gymrat.DTO.friends;

import io.swagger.v3.oas.annotations.media.Schema;


public record FriendRequestActionDTO(

        Long requestId,

        boolean accepted
) {}
