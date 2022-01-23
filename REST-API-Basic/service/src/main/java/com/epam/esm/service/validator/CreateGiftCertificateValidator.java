package com.epam.esm.service.validator;

import com.epam.esm.model.GiftCertificate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CreateGiftCertificateValidator {

    private final CreateTagValidator createTagValidator;

    public CreateGiftCertificateValidator(final CreateTagValidator createTagValidator) {
        this.createTagValidator = createTagValidator;
    }

    public void validate(final GiftCertificate giftCertificate) {

        if (StringUtils.isEmpty(giftCertificate.getName())) {
            throw new ValidationException("GiftCertificate name is required");
        }

        if (StringUtils.isEmpty(giftCertificate.getDescription())) {
            throw new ValidationException("GiftCertificate description is required");
        }

        if (giftCertificate.getPrice() == null) {
            throw new ValidationException("GiftCertificate price is required");
        }

        if (giftCertificate.getPrice() <= 0) {
            throw new ValidationException("GiftCertificate price is non-positive");
        }

        if (giftCertificate.getDuration() == null) {
            throw new ValidationException("GiftCertificate duration is required");
        }

        if (giftCertificate.getDuration() <= 0) {
            throw new ValidationException("GiftCertificate duration is non-positive");
        }

        if (giftCertificate.getTags() != null) {
            giftCertificate.getTags().forEach(createTagValidator::validate);
        }
    }
}
