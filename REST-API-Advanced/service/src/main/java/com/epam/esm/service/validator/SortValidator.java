package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class SortValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;

    public void sortValidate(final String sort) {

        if (sort != null && sort.isEmpty()) {
            throw new ValidationException(rb.getString("sort.empty"),
                    HttpStatus.BAD_REQUEST, properties.getOrder());
        }

        if (sort != null
                && !sort.equalsIgnoreCase("ASC")
                && !sort.equalsIgnoreCase("DESC")) {
            throw new ValidationException(rb.getString("invalid.value"),
                    HttpStatus.BAD_REQUEST, properties.getOrder());
        }
    }
}
