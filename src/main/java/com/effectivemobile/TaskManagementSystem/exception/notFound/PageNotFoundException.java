package com.effectivemobile.TaskManagementSystem.exception.notFound;

public class PageNotFoundException extends RuntimeException{
    public PageNotFoundException(String msg){
        super(msg);
    }
}
