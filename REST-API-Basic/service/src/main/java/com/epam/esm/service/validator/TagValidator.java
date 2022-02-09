package com.epam.esm.service.validator;

import com.epam.esm.service.dto.TagDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class TagValidator {

    private ResourceBundle rb;
    public void setRb(ResourceBundle rb) {
        this.rb = rb;
    }

    public void createValidate(final TagDto tagDto) {
        if (StringUtils.isEmpty(tagDto.getName())) {
            throw new ValidationException(rb.getString("validator.tag.name.required"));
        }
    }
}
