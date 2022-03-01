package com.epam.esm.service;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {

    private final HttpStatus status;
    private final String postfix;

    public ServiceException(final String message, final HttpStatus status, String postfix, final Object... args) {
        super(String.format(message, args));
        this.status = status;
        this.postfix = postfix;
    }
}
