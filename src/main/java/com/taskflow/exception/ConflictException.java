package com.taskflow.exception;

// Thrown when a resource already exists — maps to HTTP 409
public class ConflictException extends TaskFlowException {

    public ConflictException(String message) {
        super(message, "CONFLICT");
    }
}