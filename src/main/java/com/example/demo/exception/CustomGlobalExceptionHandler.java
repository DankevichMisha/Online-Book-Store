package com.example.demo.exception;

public class CustomGlobalExceptionHandler extends RuntimeException {
    public CustomGlobalExceptionHandler(String message) {
        super(message);
    }
}
