package com.epam.esm.service.validator;

import com.epam.esm.service.DummyRb;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PaginationValidatorTest {

    private final DummyRb dummyRb = new DummyRb();
    private final PaginationValidator paginationValidator = new PaginationValidator();

    @Test
    void shouldThrowException_On_SortValidator_ForNegativePage() {
        //When
        paginationValidator.setRb(dummyRb);
        dummyRb.setMessage("invalid.pagination", "Invalid pagination");
        final ValidationException validationException = assertThrows(ValidationException.class,
                () -> paginationValidator.paginationValidate(-1, null));
        //Then
        assertEquals("Invalid pagination", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_SortValidator_ForNegativeSize() {
        //When
        paginationValidator.setRb(dummyRb);
        dummyRb.setMessage("invalid.pagination", "Invalid pagination");
        final ValidationException validationException = assertThrows(ValidationException.class,
                () -> paginationValidator.paginationValidate(null, -1));
        //Then
        assertEquals("Invalid pagination", validationException.getMessage());
    }

    @Test
    void shouldPath_On_CreateTagValidator_ForEmptyName() {
        //Given
        paginationValidator.setRb(dummyRb);
        //When
        paginationValidator.paginationValidate(1, 100);
    }
}