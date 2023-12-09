package com.effectivemobile.TaskManagementSystem.exception;

public class InvalidTokenRequestException extends RuntimeException {

    public InvalidTokenRequestException(String message) {
        super(message);
    }
}