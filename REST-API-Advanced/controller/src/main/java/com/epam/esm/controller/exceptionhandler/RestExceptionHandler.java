package com.epam.esm.controller.exceptionhandler;

import com.epam.esm.service.ServiceException;
import com.epam.esm.service.validator.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ResourceBundle;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResourceBundle rb;

    public void setRb(ResourceBundle rb) {
        this.rb = rb;
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiError> handleValidationException(final ServiceException e) {
        final HttpStatus status = e.getStatus();
        final ApiError apiError = new ApiError(status.value(), e.getMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleNotFoundException(final ValidationException e) {
        final HttpStatus status = e.getStatus();
        final ApiError apiError = new ApiError(status.value(), e.getMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleExceptionInternal(final Exception e,
                                                             final Object body,
                                                             final HttpHeaders headers,
                                                             final HttpStatus status,
                                                             final WebRequest request) {
        final ApiError error = new ApiError(status.value(), rb.getString("invalid.value"));
        return super.handleExceptionInternal(e, error, headers, status, request);
    }
}
