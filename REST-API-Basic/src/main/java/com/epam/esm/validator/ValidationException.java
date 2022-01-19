package com.epam.esm.validator;

public class ValidationException extends RuntimeException {

    public ValidationException(final String message, final Object... args) {
        super(String.format(message, args));
    }
}
