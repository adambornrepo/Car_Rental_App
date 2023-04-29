package com.g4.exception;

public class UniqueValueAlreadyExistException extends RuntimeException{
    public UniqueValueAlreadyExistException(String message) {
        super(message);
    }
}
