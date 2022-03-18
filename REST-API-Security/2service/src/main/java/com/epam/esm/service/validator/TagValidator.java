package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class TagValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;

    public void validateId(final Integer id) {
        if (id != null && id <= 0) {
            throwValidationException("id.non");
        }
    }

    public void validate(final TagDto tagDto) {
        if (tagDto == null) {
            throwValidationException("validator.tag.null");
        }

        if (tagDto.getId() != null) {
            throwValidationException("id.should.not.passed");
        }

        final String name = tagDto.getName();
        if (ObjectUtils.isEmpty(name)) {
            throwValidationException("validator.tag.name.required");
        }
    }

    private void throwValidationException(final String rbKey) {
        throw new ValidationException(rb.getString(rbKey), HttpStatus.BAD_REQUEST, properties.getTag());
    }
}
