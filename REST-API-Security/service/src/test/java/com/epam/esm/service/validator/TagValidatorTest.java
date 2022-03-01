package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TagValidatorTest {

    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private TagValidator tagValidator;

    private ResourceBundle rb;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(tagValidator, "rb", rb);
    }

    @Test
    void shouldThrowException_On_CreateTagValidator_ForEmptyName() {
        //Given
        final TagDto tagDto = new TagDto();
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> tagValidator.validate(tagDto));
        //Then
        assertEquals(rb.getString("validator.tag.name.required"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_CreateTagValidator_ForPassedId() {
        //When
        final TagDto tagDto = new TagDto(1);
        final ValidationException validationException
                = assertThrows(
                        ValidationException.class, () -> tagValidator.validate(tagDto));
        //Then
        assertEquals(rb.getString("id.value.passed"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_ValidateId() {
        //When
        final int id = -1;
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> tagValidator.validateId(id));
        //Then
        assertEquals(rb.getString("id.non"), validationException.getMessage());
    }
}