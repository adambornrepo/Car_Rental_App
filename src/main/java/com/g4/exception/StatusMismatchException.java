package com.g4.exception;

public class StatusMismatchException extends RuntimeException{
    public StatusMismatchException(String message) {
        super(message);
    }
}
