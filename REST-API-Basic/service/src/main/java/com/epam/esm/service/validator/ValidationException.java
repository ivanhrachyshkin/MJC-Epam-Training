package com.epam.esm.service.validator;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {

    private final HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    public ValidationException(final String message, final HttpStatus status, final Object... args) {
        super(String.format(message, args));
        this.status = status;
    }
}
