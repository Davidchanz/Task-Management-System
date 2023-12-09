package com.effectivemobile.TaskManagementSystem.exception;

public class RequiredRequestParamIsMissingException extends RuntimeException{
    public RequiredRequestParamIsMissingException(String message){
        super(message);
    }
}
