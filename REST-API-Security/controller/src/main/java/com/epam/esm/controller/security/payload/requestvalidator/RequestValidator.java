package com.epam.esm.controller.security.payload.requestvalidator;

import com.epam.esm.controller.security.payload.LoginRequest;
import com.epam.esm.controller.security.payload.TokenRefreshRequest;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.validator.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;

    public void validateLoginRequest(final LoginRequest request) {

        if (request == null) {
            throw new ValidationException(rb.getString("validation.request.null"), HttpStatus.BAD_REQUEST, properties.getReq());
        }

        if (request.getUsername() == null) {
            throw new ValidationException(rb.getString("validation.request.username.null"), HttpStatus.BAD_REQUEST, properties.getReq());
        }

        if (request.getPassword() == null) {
            throw new ValidationException(rb.getString("validation.request.password.null"), HttpStatus.BAD_REQUEST, properties.getReq());
        }

        if (request.getUsername().isEmpty()) {
            throw new ValidationException(rb.getString("validation.request.username.empty"), HttpStatus.BAD_REQUEST, properties.getReq());
        }

        if (request.getPassword().isEmpty()) {
            throw new ValidationException(rb.getString("validation.request.password.empty"), HttpStatus.BAD_REQUEST, properties.getReq());
        }

    }

    public void validateRefreshTokenRequest(final TokenRefreshRequest request) {

        if (request == null) {
            throw new ValidationException(rb.getString("validation.request.null"), HttpStatus.BAD_REQUEST, properties.getReq());
        }

        if (request.getRefreshToken() == null) {
            throw new ValidationException(rb.getString("validation.request.token.null"), HttpStatus.BAD_REQUEST, properties.getReq());
        }

        if (request.getRefreshToken().isEmpty()) {
            throw new ValidationException(rb.getString("validation.request.token.empty"), HttpStatus.BAD_REQUEST, properties.getReq());
        }
    }
}

