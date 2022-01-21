package com.epam.esm.service.validator;

import com.epam.esm.model.GiftCertificate;
import org.springframework.stereotype.Component;

@Component
public class UpdateGiftCertificateValidator {

    public void validate(final GiftCertificate giftCertificate) {

        if (giftCertificate.getName() != null && giftCertificate.getName().isEmpty()) {
            throw new ValidationException("Tag name is empty");
        }

        if (giftCertificate.getDescription() != null && giftCertificate.getDescription().isEmpty()) {
            throw new ValidationException("Tag description is empty");
        }

        if (giftCertificate.getPrice() != null && giftCertificate.getPrice() <= 0) {
            throw new ValidationException("Tag price is non-positive");
        }

        if (giftCertificate.getDuration() != null && giftCertificate.getDuration() <= 0) {
            throw new ValidationException("Tag duration is non-positive");
        }
    }
}
