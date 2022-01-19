package com.epam.esm.validator;

import com.epam.esm.model.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CreateTagValidator {

    public void validate(final Tag tag) {
        if(StringUtils.isEmpty(tag.getName())) {
            throw new ValidationException("Tag name is required");
        }
    }
}
