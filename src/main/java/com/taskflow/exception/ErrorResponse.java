package com.taskflow.exception;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;

    public static ErrorResponse of(int status, String error,
                                   String message, String path) {
        ErrorResponse response  = new ErrorResponse();
        response.status         = status;
        response.error          = error;
        response.message        = message;
        response.path           = path;
        response.timestamp      = LocalDateTime.now();
        return response;
    }
}
