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

        if (userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
            throw new ValidationException(rb.getString("user.name.required"),
                    HttpStatus.BAD_REQUEST, properties.getUser());
        }

        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new ValidationException(rb.getString("user.email.required"),
                    HttpStatus.BAD_REQUEST, properties.getUser());
        }

        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new ValidationException(rb.getString("user.password.required"),
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
