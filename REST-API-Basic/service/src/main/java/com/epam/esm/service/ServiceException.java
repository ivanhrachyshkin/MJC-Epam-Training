package com.epam.esm.service;

public class ServiceException extends RuntimeException {

    public ServiceException(final String message, final Object... args) {
        super(String.format(message, args));
    }
}
