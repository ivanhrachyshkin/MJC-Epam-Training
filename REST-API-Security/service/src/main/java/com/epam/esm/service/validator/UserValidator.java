package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class UserValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;

    public void validateId(final Integer id) {
        if (id != null && id <= 0)
            throwValidationException("validator.id.non");
    }

    public void createValidate(final UserDto userDto) {
        validateNullOrEmpty(userDto, "validator.user.null");
        validateNotNullOrNotEmpty(userDto.getId(), "validator.id.should.not.passed");
        validateNullOrEmpty(userDto.getUsername(), "validator.user.name.required");
        validateNullOrEmpty(userDto.getEmail(), "validator.user.email.required");
        validateNullOrEmpty(userDto.getPassword(), "validator.user.password.required");
    }

    private void validateNullOrEmpty(final Object field, final String rbKey) {
        if (ObjectUtils.isEmpty(field)) {
            throwValidationException(rbKey);
        }
    }

    private void validateNotNullOrNotEmpty(final Object field, final String rbKey) {
        if (!ObjectUtils.isEmpty(field)) {
            throwValidationException(rbKey);
        }
    }

    private void throwValidationException(final String rbKey) {
        throw new ValidationException(rb.getString(rbKey), HttpStatus.BAD_REQUEST, properties.getUser());
    }
}
