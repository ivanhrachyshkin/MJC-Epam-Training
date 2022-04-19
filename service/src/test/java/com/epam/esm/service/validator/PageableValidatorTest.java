package com.epam.esm.service.validator;

import com.epam.esm.service.AssertionsProvider;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageableValidatorTest extends AssertionsProvider {

    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private PageableValidator paginationValidator;

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
        final ValidationException expectedException = getException(rb.getString("validator.null.pagination"));
        //When
        final ValidationException actualException
                = assertThrows(
                ValidationException.class, () -> paginationValidator.paginationValidate(pageable));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }

    @Test
    void shouldThrowException_For_NegativePageSize() {
        //Given
        when(pageable.getPageSize()).thenReturn(-1);
        final ValidationException expectedException = getException(rb.getString("validator.negative.pagination"));
        //When
        final ValidationException actualException
                = assertThrows(
                ValidationException.class, () -> paginationValidator.paginationValidate(pageable));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }

    @Test
    void shouldThrowException_For_NegativePageNumber() {
        //Given
        when(pageable.getPageNumber()).thenReturn(-1);
        final ValidationException expectedException = getException(rb.getString("validator.negative.pagination"));
        //When
        final ValidationException actualException
                = assertThrows(
                ValidationException.class, () -> paginationValidator.paginationValidate(pageable));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }

    private ValidationException getException(final String message) {
        return new ValidationException(message, HttpStatus.BAD_REQUEST, properties.getPagination());
    }
}
