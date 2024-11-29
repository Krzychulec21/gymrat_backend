package com.example.gymrat.exception.user;

public class UserAlreadyExistsException extends RuntimeException {
    private final String errorCode;

    public UserAlreadyExistsException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
