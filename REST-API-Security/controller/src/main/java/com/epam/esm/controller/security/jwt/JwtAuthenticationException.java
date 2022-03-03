package com.epam.esm.controller.security.jwt;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private final HttpStatus status;
    private final String postfix;

    public JwtAuthenticationException(final String message,
                                      final HttpStatus status,
                                      final String postfix,
                                      final Object... args) {
        super(String.format(message, args));
        this.status = status;
        this.postfix = postfix;
    }
}
