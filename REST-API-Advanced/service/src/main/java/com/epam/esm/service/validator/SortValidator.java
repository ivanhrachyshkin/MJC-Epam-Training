package com.epam.esm.service.validator;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class SortValidator {

    private static final String POSTFIX = "01";

    @Setter
    private ResourceBundle rb;

    public void sortValidate(final String sort) {

        if (sort != null && sort.isEmpty()) {
            throw new ValidationException(rb.getString("sort.empty"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (sort != null && !sort.equalsIgnoreCase("ASC") && !sort.equalsIgnoreCase("DESC")) {
            throw new ValidationException(rb.getString("invalid.value"), HttpStatus.BAD_REQUEST, POSTFIX);
        }
    }
}
