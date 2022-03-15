package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class UserValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;

    public void validateId(final Integer id) {
        if (id != null && id <= 0)
            throwValidationException("id.non");
    }

    public void createValidate(final UserDto userDto) {
        if (userDto == null) {
            throwValidationException("validator.user.null");
        }
        if (userDto.getId() != null) {
            throwValidationException("id.should.not.passed");
        }
        final Map<Object, String> fieldsWithRbKeys = getFieldWithRbKey(userDto);
        fieldsWithRbKeys.forEach(this::validateNullOrEmpty);
    }

    private Map<Object, String> getFieldWithRbKey(final UserDto userDto) {
        final Map<Object, String> values = new HashMap<>();
        values.put(userDto.getUsername(), "validator.user.name.required");
        values.put(userDto.getEmail(), "validator.user.email.required");
        values.put(userDto.getPassword(), "validator.user.password.required");
        return values;
    }

    private void validateNullOrEmpty(final Object field, final String rbKey) {
        if (ObjectUtils.isEmpty(field)) {
            throwValidationException(rbKey);
        }
    }

    private void throwValidationException(final String rbKey) {
        throw new ValidationException(rb.getString(rbKey), HttpStatus.BAD_REQUEST, properties.getUser());
    }
}
