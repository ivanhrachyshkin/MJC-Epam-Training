package com.epam.esm.service.validator;

import org.springframework.stereotype.Component;

@Component
public class ReadAllGiftCertificatesValidator {

    public void validate(final String tag,
                         final String name,
                         final String description) {

        if (tag != null && tag.isEmpty()) {
            throw new ValidationException("Tag name is required");
        }

        if (name != null && name.isEmpty()) {
            throw new ValidationException("GiftCertificate name is required");
        }

        if (description != null && description.isEmpty()) {
            throw new ValidationException("GiftCertificate description is required");
        }
    }
}
