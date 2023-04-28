package com.g4.exception;

public class PlateNumberAlreadyExistException extends RuntimeException{
    public PlateNumberAlreadyExistException(String message) {
        super(message);
    }
}
