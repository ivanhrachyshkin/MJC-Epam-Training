package com.epam.esm.service.validator;

import com.epam.esm.model.GiftCertificate;
import org.springframework.stereotype.Component;

@Component
public class UpdateGiftCertificateValidator {

    public void validate(final GiftCertificate giftCertificate) {

        if (giftCertificate.getName() != null && giftCertificate.getName().isEmpty()) {
            throw new ValidationException("GiftCertificate name is empty");
        }

        if (giftCertificate.getDescription() != null && giftCertificate.getDescription().isEmpty()) {
            throw new ValidationException("GiftCertificate description is empty");
        }

        if (giftCertificate.getPrice() != null && giftCertificate.getPrice() <= 0) {
            throw new ValidationException("GiftCertificate price is non-positive");
        }

        if (giftCertificate.getDuration() != null && giftCertificate.getDuration() <= 0) {
            throw new ValidationException("GiftCertificate duration is non-positive");
        }
    }
}
