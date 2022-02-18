package com.epam.esm.service.validator;

import com.epam.esm.service.dto.TagDto;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ResourceBundle;

@Component
public class TagValidator {

    private static final String POSTFIX = "02";

    @Setter
    private ResourceBundle rb;

    public void createValidate(final TagDto tagDto) {

        if(tagDto.getId() != null) {
            throw new ValidationException(
                    rb.getString("id.value.passed"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (tagDto.getName() == null || tagDto.getName().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.tag.name.required"), HttpStatus.BAD_REQUEST, POSTFIX);
        }
    }
}
