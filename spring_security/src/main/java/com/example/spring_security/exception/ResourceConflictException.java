package com.example.spring_security.exception;

public class ResourceConflictException extends Exception{
    public ResourceConflictException(String message) {
        super(message);
    }
}
