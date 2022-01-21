package com.epam.esm.service.validator;

public class ValidationException extends RuntimeException {

    public ValidationException(final String message, final Object... args) {
        super(String.format(message, args));
    }
}
