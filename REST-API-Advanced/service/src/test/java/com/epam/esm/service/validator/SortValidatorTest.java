package com.epam.esm.service.validator;

import com.epam.esm.service.DummyRb;
import com.epam.esm.service.dto.TagDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SortValidatorTest {

    private final DummyRb dummyRb = new DummyRb();
    private final SortValidator sortValidator = new SortValidator();

    @Test
    void shouldThrowException_On_SortValidator_ForEmptySort() {
        //When
        sortValidator.setRb(dummyRb);
        dummyRb.setMessage("sort.empty", "Empty sort");
        final String sort = "";
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> sortValidator.sortValidate(sort));
        //Then
        assertEquals("Empty sort", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_SortValidator_ForInvalid() {
        //When
        sortValidator.setRb(dummyRb);
        dummyRb.setMessage("invalid.value", "Invalid value");
        final String sort = "aaa";
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> sortValidator.sortValidate(sort));
        //Then
        assertEquals("Invalid value", validationException.getMessage());
    }

    @Test
    void shouldPath_On_CreateTagValidator_ForEmptyName() {
        //Given
        sortValidator.setRb(dummyRb);
        //When
        sortValidator.sortValidate("desc");
    }
}