package com.effectivemobile.TaskManagementSystem.exception.exist;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
