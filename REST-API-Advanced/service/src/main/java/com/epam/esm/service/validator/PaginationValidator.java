package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class PaginationValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;

    public void paginationValidate(final Integer page, final Integer size) {

        if (page == null || size == null) {
            throw new ValidationException(
                    rb.getString("invalid.pagination"),
                    HttpStatus.BAD_REQUEST, properties.getPagination());
        }

        if (page < 1) {
            throw new ValidationException(
                    rb.getString("invalid.pagination"),
                    HttpStatus.BAD_REQUEST, properties.getPagination());
        }

        if (size < 1) {
            throw new ValidationException(
                    rb.getString("invalid.pagination"),
                    HttpStatus.BAD_REQUEST, properties.getPagination());
        }
    }
}
