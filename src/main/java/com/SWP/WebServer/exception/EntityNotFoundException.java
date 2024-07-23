package com.SWP.WebServer.exception;

// Custom exception class
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
