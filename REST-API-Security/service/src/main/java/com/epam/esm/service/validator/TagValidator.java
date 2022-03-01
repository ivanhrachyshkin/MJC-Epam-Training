package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class TagValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;

    public void validate(final TagDto tagDto) {

        if(tagDto.getId() != null) {
            throw new ValidationException(
                    rb.getString("id.value.passed"),
                    HttpStatus.BAD_REQUEST, properties.getTag());
        }

        if (tagDto.getName() == null || tagDto.getName().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.tag.name.required"),
                    HttpStatus.BAD_REQUEST, properties.getTag());
        }
    }

    public void validateId(final int id) {
        if (id < 1) {
            throw new ValidationException(rb.getString("id.non"),
                    HttpStatus.BAD_REQUEST, properties.getTag());
        }
    }
}
