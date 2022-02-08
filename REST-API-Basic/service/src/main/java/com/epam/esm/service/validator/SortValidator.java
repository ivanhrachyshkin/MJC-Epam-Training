package com.epam.esm.service.validator;

import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class SortValidator {

    private final ResourceBundle rb;

    public SortValidator(final ResourceBundle rb) {
        this.rb = rb;
    }

    public void sortValidate(final String sort) {

        if (sort != null && sort.isEmpty()) {
            throw new ValidationException(rb.getString("sort.empty"));
        }

        if (sort != null && !sort.equals("ASC") && !sort.equals("DESC")) {
            throw new ValidationException(rb.getString("sort.invalid"));
        }
    }
}
