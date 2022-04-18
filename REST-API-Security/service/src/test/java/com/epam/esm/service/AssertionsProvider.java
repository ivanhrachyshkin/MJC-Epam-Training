package com.epam.esm.service;

import com.epam.esm.service.validator.ValidationException;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertionsProvider<T> {

    public void assertValidationExceptions(final ValidationException expected, final ValidationException actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPostfix(), actual.getPostfix());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    public void assertServiceExceptions(final ServiceException expected, final ServiceException actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPostfix(), actual.getPostfix());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    public void assertPages(Page<T> expected, Page<T> actual) {
        assertEquals(expected, actual);
        assertEquals(expected.getTotalElements(), actual.getTotalElements());
        assertEquals(expected.getTotalPages(), actual.getTotalPages());
    }
}
