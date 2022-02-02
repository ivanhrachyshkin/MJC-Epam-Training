package com.epam.esm.service.validator;

import com.epam.esm.service.dto.TagDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TagValidator {

    public void createValidate(final TagDto tagDto) {
        if (StringUtils.isEmpty(tagDto.getName())) {
            throw new ValidationException("validator.tag.name.required");
        }
    }
}
