package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
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
class PaginationValidatorTest {

    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private PaginationValidator paginationValidator;

    private ResourceBundle rb;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(paginationValidator, "rb", rb);
    }

    @Test
    void shouldThrowException_On_SortValidator_ForNegativePage() {
        //When
        final ValidationException validationException = assertThrows(ValidationException.class,
                () -> paginationValidator.paginationValidate(-1, null));
        //Then
        assertEquals(rb.getString("invalid.pagination"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_SortValidator_ForNegativeSize() {
        //When
        final ValidationException validationException = assertThrows(ValidationException.class,
                () -> paginationValidator.paginationValidate(null, -1));
        //Then
        assertEquals(rb.getString("invalid.pagination"), validationException.getMessage());
    }

    @Test
    void shouldPath_On_CreateTagValidator_ForEmptyName() {
        //Given
        //When
        paginationValidator.paginationValidate(1, 100);
    }
}