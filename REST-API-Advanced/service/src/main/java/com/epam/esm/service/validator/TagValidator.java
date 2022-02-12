package com.epam.esm.service.validator;

import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class TagValidator {

    private final ResourceBundle rb;

    public void createValidate(final TagDto tagDto) {
        if (StringUtils.isEmpty(tagDto.getName())) {
            throw new ValidationException(rb.getString("validator.tag.name.required"), HttpStatus.BAD_REQUEST);
        }
    }
}
