package com.epam.esm.service.validator;

import com.epam.esm.service.dto.TagDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TagValidator {

    public void createValidate(final TagDto tagDto) {
        if (StringUtils.isEmpty(tagDto.getName())) {
            throw new ValidationException("Tag name is required");
        }
    }
}
