package com.epam.esm.controller.security.payload.requestvalidator;

import com.epam.esm.controller.security.payload.LoginRequest;
import com.epam.esm.controller.security.payload.TokenRefreshRequest;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.validator.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
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
            throwValidationException("validation.request.null");
        }
        if (ObjectUtils.isEmpty(request.getUsername())) {
         throwValidationException("validation.request.username.required");
        }
        if (ObjectUtils.isEmpty(request.getPassword())) {
           throwValidationException("validation.request.password.required");
        }
    }

    public void validateRefreshTokenRequest(final TokenRefreshRequest request) {
        if (request == null) {
            throwValidationException("validation.request.null");
        }
        if (ObjectUtils.isEmpty(request.getRefreshToken())) {
            throwValidationException("validation.request.token.required");
        }
    }

    private void throwValidationException(final String rbKey) {
        throw new ValidationException(rb.getString(rbKey), HttpStatus.BAD_REQUEST, properties.getReq());
    }
}

