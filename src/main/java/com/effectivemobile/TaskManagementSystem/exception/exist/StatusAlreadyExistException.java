package com.effectivemobile.TaskManagementSystem.exception.exist;

public class StatusAlreadyExistException extends RuntimeException{
    public StatusAlreadyExistException(String msg){
        super(msg);
    }
}
