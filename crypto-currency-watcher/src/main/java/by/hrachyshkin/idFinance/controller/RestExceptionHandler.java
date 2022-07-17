package by.hrachyshkin.idFinance.controller;

import by.hrachyshkin.idFinance.service.ServiceException;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ResourceBundle;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiError> handleServiceException(final ServiceException e) {
        final HttpStatus status = e.getStatus();
        final ApiError apiError = new ApiError(status.value(), e.getMessage());
        return new ResponseEntity<>(apiError, status);
    }
}
