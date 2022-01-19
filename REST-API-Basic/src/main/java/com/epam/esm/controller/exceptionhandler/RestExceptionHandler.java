package com.epam.esm.controller.exceptionhandler;

import com.epam.esm.model.ApiError;
import com.epam.esm.service.AlreadyExistException;
import com.epam.esm.service.NotFoundException;
import com.epam.esm.validator.ValidationException;
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


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final Exception e) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final Exception e) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleAlreadyExistException(final Exception e) {
        return new ApiError(HttpStatus.CONFLICT.value(), e.getMessage());
    }


    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleExceptionInternal(final Exception e,
                                                             final Object body,
                                                             final HttpHeaders headers,
                                                             final HttpStatus status,
                                                             final WebRequest request) {
        final ApiError error = new ApiError(status.value(), e.getMessage());
        return super.handleExceptionInternal(e, error, headers, status, request);
    }
}
