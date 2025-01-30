package com.reliaquest.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmployeeChallengeException extends RuntimeException {
    private final String message;
    private final HttpStatus status;
    private final Integer errorCode;

    public EmployeeChallengeException(String message) {
        this.message = message;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCode = null;
    }

    public EmployeeChallengeException(String message, HttpStatus status, Integer errorCode) {
        this.message = message;
        this.status = status;
        this.errorCode = errorCode;
    }

    public EmployeeChallengeException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.errorCode = null;
    }
}
