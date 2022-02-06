package com.epam.esm.service.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class GiftCertificateValidator {

    private final TagValidator tagValidator;

    public GiftCertificateValidator(final TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    public void updateValidate(final GiftCertificateDto giftCertificateDto) {

        if (giftCertificateDto.getName() != null && giftCertificateDto.getName().isEmpty()) {
            throw new ValidationException("validator.giftCertificate.name.empty");
        }

        if (giftCertificateDto.getDescription() != null && giftCertificateDto.getDescription().isEmpty()) {
            throw new ValidationException("validator.giftCertificate.description.empty");
        }

        if (giftCertificateDto.getPrice() != null && giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException("validator.giftCertificate.price.negative");
        }

        if (giftCertificateDto.getDuration() != null && giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException("validator.giftCertificate.duration.negative");
        }
    }

    public void readAllValidate(final String tag,
                                final String name,
                                final String description) {

        if (tag != null && tag.isEmpty()) {
            throw new ValidationException("validator.tag.name.empty");
        }

        if (name != null && name.isEmpty()) {
            throw new ValidationException("validator.giftCertificate.name.empty");
        }

        if (description != null && description.isEmpty()) {
            throw new ValidationException("validator.giftCertificate.description.empty");
        }
    }

    public void createValidate(final GiftCertificateDto giftCertificateDto) {

        if (StringUtils.isEmpty(giftCertificateDto.getName())) {
            throw new ValidationException("validator.giftCertificate.name.required");
        }

        if (StringUtils.isEmpty(giftCertificateDto.getDescription())) {
            throw new ValidationException("validator.giftCertificate.description.required");
        }

        if (giftCertificateDto.getPrice() == null) {
            throw new ValidationException("validator.giftCertificate.price.required");
        }

        if (giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException("validator.giftCertificate.price.negative");
        }

        if (giftCertificateDto.getDuration() == null) {
            throw new ValidationException("validator.giftCertificate.duration.required");
        }

        if (giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException("validator.giftCertificate.duration.negative");
        }

        if (giftCertificateDto.getDtoTags() != null) {
            giftCertificateDto.getDtoTags().forEach(tagValidator::createValidate);
        }
    }
}
