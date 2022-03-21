package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.TagDto;
import org.apache.commons.lang3.StringUtils;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagValidatorTest {

    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private TagValidator tagValidator;

    @Mock
    private TagDto dtoTag;

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
    void shouldThrowException_On_ValidateId() {
        //When
        final int id = -1;
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> tagValidator.validateId(id));
        //Then
        assertEquals(rb.getString("validator.id.non"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_For_Null() {
        //Given
        dtoTag = null;
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> tagValidator.validate(dtoTag));
        //Then
        assertEquals(rb.getString("validator.tag.null"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_For_IdPassed() {
        //Given
        when(dtoTag.getId()).thenReturn(1);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> tagValidator.validate(dtoTag));
        //Then
        assertEquals(rb.getString("validator.id.should.not.passed"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_For_NullName() {
        //Given
        when(dtoTag.getId()).thenReturn(null);
        when(dtoTag.getName()).thenReturn(null);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> tagValidator.validate(dtoTag));
        //Then
        assertEquals(rb.getString("validator.tag.name.required"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_For_EmptyName() {
        //Given
        when(dtoTag.getId()).thenReturn(null);
        when(dtoTag.getName()).thenReturn(StringUtils.EMPTY);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> tagValidator.validate(dtoTag));
        //Then
        assertEquals(rb.getString("validator.tag.name.required"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_For_ActivePassed() {
        //Given
        when(dtoTag.getId()).thenReturn(null);
        when(dtoTag.getName()).thenReturn("name");
        when(dtoTag.getActive()).thenReturn(true);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> tagValidator.validate(dtoTag));
        //Then
        assertEquals(rb.getString("validator.active.should.not.passed"), validationException.getMessage());
    }
}