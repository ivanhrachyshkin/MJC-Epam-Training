package com.epam.esm.service.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class GiftCertificateValidator {

    private final ResourceBundle rb;
    private final TagValidator tagValidator;

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

    public void readAllValidate(final List<String> tags,
                                final String name,
                                final String description) {

        if (tags != null && !tags.isEmpty()) {
            tags.forEach(tag -> {
                if (tag == null || tag.isEmpty()) {
                    throw new ValidationException(rb.getString("validator.tag.name.empty"));
                }
            });
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

        if (giftCertificateDto.getDtoTags() != null) {
            giftCertificateDto.getDtoTags().forEach(tagValidator::createValidate);
        }
    }
}
