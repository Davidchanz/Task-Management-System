package com.effectivemobile.TaskManagementSystem.exception;

public class AccessToResourceDeniedException extends RuntimeException{
    public AccessToResourceDeniedException(String msg){
        super(msg);
    }
}
