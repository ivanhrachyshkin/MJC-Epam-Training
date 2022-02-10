package com.epam.esm.service.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class SortValidator {

    private  ResourceBundle rb;
    public void setRb(ResourceBundle rb) {
        this.rb = rb;
    }

    public void sortValidate(final String sort) {

        if (sort != null && sort.isEmpty()) {
            throw new ValidationException(rb.getString("sort.empty"), HttpStatus.BAD_REQUEST);
        }

        if (sort != null && !sort.equalsIgnoreCase("ASC") && !sort.equalsIgnoreCase("DESC")) {
            throw new ValidationException(rb.getString("invalid.value"), HttpStatus.BAD_REQUEST);
        }
    }
}
