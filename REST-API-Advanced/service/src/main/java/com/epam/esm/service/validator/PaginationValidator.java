package com.epam.esm.service.validator;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class PaginationValidator {

    private static final String POSTFIX = " pagination";

    @Setter
    private ResourceBundle rb;

    public void paginationValidate(final Integer page, final Integer size) {

        if (page != null && page < 1) {
            throw new ValidationException(
                    rb.getString("invalid.pagination"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (size != null && size < 1) {
            throw new ValidationException(
                    rb.getString("invalid.pagination"), HttpStatus.BAD_REQUEST, POSTFIX);
        }
    }
}
