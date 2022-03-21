package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private UserValidator userValidator;

    private static ResourceBundle getRb() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        return new PropertyResourceBundle(contentStream);
    }

    static Stream<Arguments> createValidateUserProvider() throws IOException {
        return Stream.of(
                Arguments.arguments(getRb().getString("validator.id.should.not.passed"),
                        1, null, null, null),
                Arguments.arguments(getRb().getString("validator.user.name.required"),
                        null, null, null, null),
                Arguments.arguments(getRb().getString("validator.user.name.required"),
                        null, StringUtils.EMPTY, null, null),
                Arguments.arguments(getRb().getString("validator.user.email.required"),
                        null, "username", null, null),
                Arguments.arguments(getRb().getString("validator.user.email.required"),
                        null, "username", StringUtils.EMPTY, null),
                Arguments.arguments(getRb().getString("validator.user.password.required"),
                        null, "username", "email", null),
                Arguments.arguments(getRb().getString("validator.user.password.required"),
                        null, "username", "email", StringUtils.EMPTY)
        );
    }

    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(userValidator, "rb", getRb());
    }

    @Test
    void shouldThrowException_ForValidateId() throws IOException {
        //Given
        final int id = -1;
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> userValidator.validateId(id));
        //Then
        assertEquals(getRb().getString("validator.id.non"), validationException.getMessage());
    }


    @ParameterizedTest
    @MethodSource("createValidateUserProvider")
    void shouldThrowException_On_CreateGiftCertificateValidator(final String expected,
                                                                final Integer id,
                                                                final String username,
                                                                final String email,
                                                                final String password) {
        //Given
        final UserDto userDto = getUserDto(id, username, email, password);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> userValidator.createValidate(userDto));
        //Then
        assertEquals(expected, validationException.getMessage());
    }

    private UserDto getUserDto(final Integer id,
                               final String username,
                               final String email,
                               final String password) {
        final UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setUsername(username);
        userDto.setEmail(email);
        userDto.setPassword(password);
        return userDto;
    }
}
