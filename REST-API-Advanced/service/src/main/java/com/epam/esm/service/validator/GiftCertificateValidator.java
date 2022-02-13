package com.epam.esm.service.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class GiftCertificateValidator {

    private static final String POSTFIX = "01";

    @Setter
    private ResourceBundle rb;
    private final TagValidator tagValidator;

    public void updateValidate(final GiftCertificateDto giftCertificateDto) {

        if (giftCertificateDto.getName() != null && giftCertificateDto.getName().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.name.empty"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (giftCertificateDto.getDescription() != null && giftCertificateDto.getDescription().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.description.empty"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (giftCertificateDto.getPrice() != null && giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.price.negative"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (giftCertificateDto.getDuration() != null && giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.duration.negative"), HttpStatus.BAD_REQUEST, POSTFIX);
        }
    }

    public void readAllValidate(final List<String> tags,
                                final String name,
                                final String description) {

        if (tags != null && !tags.isEmpty()) {
            tags.forEach(tag -> {
                if (tag == null || tag.isEmpty()) {
                    throw new ValidationException(
                            rb.getString("validator.tag.name.empty"), HttpStatus.BAD_REQUEST, POSTFIX);
                }
            });
        }

        if (name != null && name.isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.name.empty"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (description != null && description.isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.description.empty"), HttpStatus.BAD_REQUEST, POSTFIX);
        }
    }

    public void createValidate(final GiftCertificateDto giftCertificateDto) {

        if (StringUtils.isEmpty(giftCertificateDto.getName())) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.name.required"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (StringUtils.isEmpty(giftCertificateDto.getDescription())) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.description.required"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (giftCertificateDto.getPrice() == null) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.price.required"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.price.negative"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (giftCertificateDto.getDuration() == null) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.duration.required"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.duration.negative"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (giftCertificateDto.getDtoTags() != null) {
            giftCertificateDto.getDtoTags().forEach(tagValidator::createValidate);
        }
    }
}
