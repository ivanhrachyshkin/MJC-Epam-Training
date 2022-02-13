package com.epam.esm.service.validator;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidationException extends RuntimeException {

    private final HttpStatus status;
    private final String postfix;

    public HttpStatus getStatus() {
        return status;
    }

    public ValidationException(final String message, final HttpStatus status, String postfix, final Object... args) {
        super(String.format(message, args));
        this.status = status;
        this.postfix = postfix;
    }
}
