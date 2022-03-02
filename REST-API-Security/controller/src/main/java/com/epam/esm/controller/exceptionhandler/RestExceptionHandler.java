package com.epam.esm.controller.exceptionhandler;

import com.epam.esm.service.ServiceException;
import com.epam.esm.secutiry.jwt.JwtAuthenticationException;
import com.epam.esm.service.validator.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiError> handleServiceException(final ServiceException e) {
        final HttpStatus status = e.getStatus();
        final ApiError apiError = new ApiError(customizeCode(status, e.getPostfix()), e.getMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(final ValidationException e) {
        final HttpStatus status = e.getStatus();
        final ApiError apiError = new ApiError(customizeCode(status, e.getPostfix()), e.getMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleExceptionInternal(final Exception e,
                                                             final Object body,
                                                             final HttpHeaders headers,
                                                             final HttpStatus status,
                                                             final WebRequest request) {
        final ApiError error = new ApiError(customizeCode(status, ""), e.getMessage());
        return super.handleExceptionInternal(e, error, headers, status, request);
    }

    private String customizeCode(final HttpStatus status, final String postfix) {
        return status.value() + postfix;
    }
}
