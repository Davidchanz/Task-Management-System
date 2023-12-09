package com.effectivemobile.TaskManagementSystem.exception.notFound;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(String msg){
        super(msg);
    }
}
