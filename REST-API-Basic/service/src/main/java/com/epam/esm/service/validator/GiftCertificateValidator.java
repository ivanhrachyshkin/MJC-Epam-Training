package com.epam.esm.service.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class GiftCertificateValidator {

    private final ResourceBundle rb;
    private final TagValidator tagValidator;

    public GiftCertificateValidator(final ResourceBundle rb, final TagValidator tagValidator) {
        this.rb = rb;
        this.tagValidator = tagValidator;
    }

    public void updateValidate(final GiftCertificateDto giftCertificateDto) {

        if (giftCertificateDto.getName() != null && giftCertificateDto.getName().isEmpty()) {
            throw new ValidationException(rb.getString("validator.giftCertificate.name.empty"));
        }

        if (giftCertificateDto.getDescription() != null && giftCertificateDto.getDescription().isEmpty()) {
            throw new ValidationException(rb.getString("validator.giftCertificate.description.empty"));
        }

        if (giftCertificateDto.getPrice() != null && giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException(rb.getString("validator.giftCertificate.price.negative"));
        }

        if (giftCertificateDto.getDuration() != null && giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException(rb.getString("validator.giftCertificate.duration.negative"));
        }
    }

    public void readAllValidate(final String tag,
                                final String name,
                                final String description) {

        if (tag != null && tag.isEmpty()) {
            throw new ValidationException(rb.getString("validator.tag.name.empty"));
        }

        if (name != null && name.isEmpty()) {
            throw new ValidationException(rb.getString("validator.giftCertificate.name.empty"));
        }

        if (description != null && description.isEmpty()) {
            throw new ValidationException(rb.getString("validator.giftCertificate.description.empty"));
        }
    }

    public void createValidate(final GiftCertificateDto giftCertificateDto) {

        if (StringUtils.isEmpty(giftCertificateDto.getName())) {
            throw new ValidationException(rb.getString("validator.giftCertificate.name.required"));
        }

        if (StringUtils.isEmpty(giftCertificateDto.getDescription())) {
            throw new ValidationException(rb.getString("validator.giftCertificate.description.required"));
        }

        if (giftCertificateDto.getPrice() == null) {
            throw new ValidationException(rb.getString("validator.giftCertificate.price.required"));
        }

        if (giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException(rb.getString("validator.giftCertificate.price.negative"));
        }

        if (giftCertificateDto.getDuration() == null) {
            throw new ValidationException(rb.getString("validator.giftCertificate.duration.required"));
        }

        if (giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException(rb.getString("validator.giftCertificate.duration.negative"));
        }

        if (giftCertificateDto.getTags() != null) {
            giftCertificateDto.getTags().forEach(tagValidator::createValidate);
        }
    }
}
