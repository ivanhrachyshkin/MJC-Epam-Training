package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class UserValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;

    public void createValidate(final UserDto userDto) {

        if (userDto == null) {
            throw new ValidationException(rb.getString("validator.user.null"),
                    HttpStatus.BAD_REQUEST, properties.getUser());
        }

        if (userDto.getId() != null) {
            throw new ValidationException(rb.getString("id.value.passed"),
                    HttpStatus.BAD_REQUEST, properties.getUser());
        }

        if (userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
            throw new ValidationException(rb.getString("validator.user.name.required"),
                    HttpStatus.BAD_REQUEST, properties.getUser());
        }

        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new ValidationException(rb.getString("validator.user.email.required"),
                    HttpStatus.BAD_REQUEST, properties.getUser());
        }

        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new ValidationException(rb.getString("validator.user.password.required"),
                    HttpStatus.BAD_REQUEST, properties.getUser());
        }
    }

    public void validateId(final int id) {
        if (id < 1) {
            throw new ValidationException(rb.getString("id.non"),
                    HttpStatus.BAD_REQUEST, properties.getUser());
        }
    }
}
