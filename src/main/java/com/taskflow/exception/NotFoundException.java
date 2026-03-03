package com.taskflow.exception;

// Thrown when a resource doesn't exist — maps to HTTP 404
public class NotFoundException extends TaskFlowException {

    public NotFoundException(String resource, Object identifier) {
        super(resource + " not found with identifier: " + identifier, "NOT_FOUND");
    }
}