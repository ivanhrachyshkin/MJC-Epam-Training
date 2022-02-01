package com.epam.esm.service;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {

    private final HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    public ServiceException(final String message, final HttpStatus status, final Object... args) {
        super(String.format(message, args));
        this.status = status;
    }
}
