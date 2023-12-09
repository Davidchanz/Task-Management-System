package com.effectivemobile.TaskManagementSystem.exception.exist;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
