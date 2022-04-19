package com.epam.esm.service.validator;

import com.epam.esm.service.AssertionsProvider;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest extends AssertionsProvider<UserDto> {

    @InjectMocks
    private UserValidator userValidator;

    private static ResourceBundle getRb() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        return new PropertyResourceBundle(contentStream);
    }

    private static ExceptionStatusPostfixProperties getProperties() {
        return new ExceptionStatusPostfixProperties();
    }

    private static ValidationException getException(final String message) {
        return new ValidationException(message, HttpStatus.BAD_REQUEST, getProperties().getUser());
    }

    static Stream<Arguments> createValidateUserProvider() throws IOException {
        final ResourceBundle rb = getRb();
        return Stream.of(
                Arguments.arguments(
                        getException(rb.getString("validator.id.should.not.passed")),
                        1, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.user.name.required")),
                        null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.user.name.required")),
                        null, StringUtils.EMPTY, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.user.email.required")),
                        null, "username", null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.user.email.required")),
                        null, "username", StringUtils.EMPTY, null),
                Arguments.arguments(
                        getException(rb.getString("validator.user.password.required")),
                        null, "username", "email", null),
                Arguments.arguments(
                        getException(rb.getString("validator.user.password.required")),
                        null, "username", "email", StringUtils.EMPTY)
        );
    }

    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(userValidator, "rb", getRb());
        ReflectionTestUtils.setField(userValidator, "properties", getProperties());
    }

    @Test
    void shouldThrowException_ForValidateId() throws IOException {
        //Given
        final ValidationException expectedException = getException(getRb().getString("validator.id.non"));
        //When
        final ValidationException actualException
                = assertThrows(ValidationException.class, () -> userValidator.validateId(-1));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }


    @ParameterizedTest
    @MethodSource("createValidateUserProvider")
    void shouldThrowException_On_CreateGiftCertificateValidator(final ValidationException expectedException,
                                                                final Integer id,
                                                                final String username,
                                                                final String email,
                                                                final String password) {
        //Given
        final UserDto userDto = getUserDto(id, username, email, password);
        //When
        final ValidationException actualException
                = assertThrows(ValidationException.class, () -> userValidator.createValidate(userDto));
        //Then
        assertValidationExceptions(expectedException, actualException);
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
