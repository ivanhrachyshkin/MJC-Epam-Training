package com.epam.esm.service.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateValidator {

    private final TagValidator tagValidator;

    public GiftCertificateValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    public void updateValidate(final GiftCertificateDto giftCertificateDto) {

        if (giftCertificateDto.getName() != null && giftCertificateDto.getName().isEmpty()) {
            throw new ValidationException("GiftCertificate name is empty");
        }

        if (giftCertificateDto.getDescription() != null && giftCertificateDto.getDescription().isEmpty()) {
            throw new ValidationException("GiftCertificate description is empty");
        }

        if (giftCertificateDto.getPrice() != null && giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException("GiftCertificate price is non-positive");
        }

        if (giftCertificateDto.getDuration() != null && giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException("GiftCertificate duration is non-positive");
        }
    }

    public void readAllValidate(final String tag,
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

    public void createValidate(final GiftCertificateDto giftCertificateDto) {

        if (StringUtils.isEmpty(giftCertificateDto.getName())) {
            throw new ValidationException("GiftCertificate name is required");
        }

        if (StringUtils.isEmpty(giftCertificateDto.getDescription())) {
            throw new ValidationException("GiftCertificate description is required");
        }

        if (giftCertificateDto.getPrice() == null) {
            throw new ValidationException("GiftCertificate price is required");
        }

        if (giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException("GiftCertificate price is non-positive");
        }

        if (giftCertificateDto.getDuration() == null) {
            throw new ValidationException("GiftCertificate duration is required");
        }

        if (giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException("GiftCertificate duration is non-positive");
        }

        if (giftCertificateDto.getDtoTags() != null) {
            giftCertificateDto.getDtoTags().forEach(tagValidator::createValidate);
        }
    }
}
