package com.example.gymrat.exception.friend;

public class FriendRequestAlreadyExistsException extends RuntimeException {
    public FriendRequestAlreadyExistsException(String message) {
        super(message);
    }
}
