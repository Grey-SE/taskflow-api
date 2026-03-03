package com.taskflow.exception;

// All our custom exceptions extend this
// RuntimeException = unchecked, callers don't have to declare "throws"
public class TaskFlowException extends RuntimeException {

    private final String errorCode;

    public TaskFlowException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}