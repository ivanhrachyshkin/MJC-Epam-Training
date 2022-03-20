package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageValidatorTest {

    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private PageValidator paginationValidator;

    @Mock
    private Pageable pageable;

    private ResourceBundle rb;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream
                = Thread.currentThread().getContextClassLoader().getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(paginationValidator, "rb", rb);
    }

    @Test
    void shouldThrowException_For_Null() {
        //Given
        pageable = null;
        //When
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> paginationValidator.paginationValidate(pageable));
        //Then
        assertEquals(rb.getString("validator.null.pagination"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_For_NegativePageSize() {
        //Given
        when(pageable.getPageSize()).thenReturn(-1);
        //When
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> paginationValidator.paginationValidate(pageable));
        //Then
        assertEquals(rb.getString("validator.negative.pagination"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_For_NegativePageNumber() {
        //Given
        when(pageable.getPageNumber()).thenReturn(-1);
        //When
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> paginationValidator.paginationValidate(pageable));
        //Then
        assertEquals(rb.getString("validator.negative.pagination"), validationException.getMessage());
    }
}
