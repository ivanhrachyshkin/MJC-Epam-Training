package com.epam.esm.service.validator;

import com.epam.esm.service.DummyRb;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.validator.TagValidator;
import com.epam.esm.service.validator.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TagValidatorTest {

    private final DummyRb dummyRb = new DummyRb();
    private final TagValidator tagValidator = new TagValidator();

    @Test
    void shouldThrowException_On_CreateTagValidator_ForEmptyName() {
        //When
        tagValidator.setRb(dummyRb);
        dummyRb.setMessage("validator.tag.name.required", "Tag name is required");
        final TagDto tagDto = new TagDto();
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> tagValidator.createValidate(tagDto));
        //Then
        assertEquals("Tag name is required", validationException.getMessage());
    }

    @Test
    void shouldPath_On_CreateTagValidator_ForEmptyName() {
        //Given
        tagValidator.setRb(dummyRb);
        final TagDto tagDto = new TagDto();
        tagDto.setName("name");
        //When
        tagValidator.createValidate(tagDto);
    }
}