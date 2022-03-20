package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.UserDto;
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
class UserValidatorTest {

    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private UserValidator userValidator;

    @Mock
    private UserDto dtoUser;

    private ResourceBundle rb;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(userValidator, "rb", rb);
    }

    @Test
    void shouldThrowException_ForValidateId() {
        //Given
        final int id = -1;
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> userValidator.validateId(id));
        //Then
        assertEquals(rb.getString("validator.id.non"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_ForNull() {
        //Given
        dtoUser = null;
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> userValidator.createValidate(dtoUser));
        //Then
        assertEquals(rb.getString("validator.user.null"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_ForPassedId() {
        //Given
        when(dtoUser.getId()).thenReturn(1);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> userValidator.createValidate(dtoUser));
        //Then
        assertEquals(rb.getString("validator.id.should.not.passed"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_ForUserNameNull() {
        //Given
        when(dtoUser.getId()).thenReturn(null);
        when(dtoUser.getUsername()).thenReturn(null);
        when(dtoUser.getEmail()).thenReturn("password");
        when(dtoUser.getPassword()).thenReturn("password");
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> userValidator.createValidate(dtoUser));
        //Then
        assertEquals(rb.getString("validator.user.name.required"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_ForUserNameEmpty() {
        //Given
        when(dtoUser.getId()).thenReturn(null);
        when(dtoUser.getUsername()).thenReturn(StringUtils.EMPTY);
        when(dtoUser.getEmail()).thenReturn("email");
        when(dtoUser.getPassword()).thenReturn("password");
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> userValidator.createValidate(dtoUser));
        //Then
        assertEquals(rb.getString("validator.user.name.required"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_ForEmailNull() {
        //Given
        when(dtoUser.getId()).thenReturn(null);
        when(dtoUser.getUsername()).thenReturn("name");
        when(dtoUser.getEmail()).thenReturn(null);
        when(dtoUser.getPassword()).thenReturn("password");
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> userValidator.createValidate(dtoUser));
        //Then
        assertEquals(rb.getString("validator.user.email.required"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_ForEmailEmpty() {
        //Given
        when(dtoUser.getId()).thenReturn(null);
        when(dtoUser.getUsername()).thenReturn("name");
        when(dtoUser.getEmail()).thenReturn(StringUtils.EMPTY);
        when(dtoUser.getPassword()).thenReturn("password");
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> userValidator.createValidate(dtoUser));
        //Then
        assertEquals(rb.getString("validator.user.email.required"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_ForPasswordNull() {
        //Given
        when(dtoUser.getId()).thenReturn(null);
        when(dtoUser.getUsername()).thenReturn("name");
        when(dtoUser.getEmail()).thenReturn("email");
        when(dtoUser.getPassword()).thenReturn(null);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> userValidator.createValidate(dtoUser));
        //Then
        assertEquals(rb.getString("validator.user.password.required"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_ForPasswordEmpty() {
        //Given
        when(dtoUser.getId()).thenReturn(null);
        when(dtoUser.getUsername()).thenReturn("name");
        when(dtoUser.getEmail()).thenReturn("email");
        when(dtoUser.getPassword()).thenReturn(null);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> userValidator.createValidate(dtoUser));
        //Then
        assertEquals(rb.getString("validator.user.password.required"), validationException.getMessage());
    }
}
