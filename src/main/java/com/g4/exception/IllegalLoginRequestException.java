package com.g4.exception;

public class IllegalLoginRequestException extends RuntimeException{
    public IllegalLoginRequestException(String message) {
        super(message);
    }
}
