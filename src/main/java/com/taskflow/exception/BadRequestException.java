package com.taskflow.exception;

// Thrown for invalid input — maps to HTTP 400
public class BadRequestException extends TaskFlowException {

    public BadRequestException(String message) {
        super(message, "BAD_REQUEST");
    }
}