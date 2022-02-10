package com.epam.esm.service.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class GiftCertificateValidator {

    private  ResourceBundle rb;
    private final TagValidator tagValidator;

    public GiftCertificateValidator(final TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    public void setRb(ResourceBundle rb) {
        this.rb = rb;
    }

    public void updateValidate(final GiftCertificateDto giftCertificateDto) {

        if (giftCertificateDto.getName() != null && giftCertificateDto.getName().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.name.empty"), HttpStatus.BAD_REQUEST);
        }

        if (giftCertificateDto.getDescription() != null && giftCertificateDto.getDescription().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.description.empty"), HttpStatus.BAD_REQUEST);
        }

        if (giftCertificateDto.getPrice() != null && giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.price.negative"), HttpStatus.BAD_REQUEST);
        }

        if (giftCertificateDto.getDuration() != null && giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.duration.negative"), HttpStatus.BAD_REQUEST);
        }
    }

    public void readAllValidate(final String tag,
                                final String name,
                                final String description) {

        if (tag != null && tag.isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.tag.name.empty"), HttpStatus.BAD_REQUEST);
        }

        if (name != null && name.isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.name.empty"), HttpStatus.BAD_REQUEST);
        }

        if (description != null && description.isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.description.empty"), HttpStatus.BAD_REQUEST);
        }
    }

    public void createValidate(final GiftCertificateDto giftCertificateDto) {

        if (StringUtils.isEmpty(giftCertificateDto.getName())) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.name.required"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isEmpty(giftCertificateDto.getDescription())) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.description.required"), HttpStatus.BAD_REQUEST);
        }

        if (giftCertificateDto.getPrice() == null) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.price.required"), HttpStatus.BAD_REQUEST);
        }

        if (giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.price.negative"), HttpStatus.BAD_REQUEST);
        }

        if (giftCertificateDto.getDuration() == null) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.duration.required"), HttpStatus.BAD_REQUEST);
        }

        if (giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.duration.negative"), HttpStatus.BAD_REQUEST);
        }

        if (giftCertificateDto.getTags() != null) {
            giftCertificateDto.getTags().forEach(tagValidator::createValidate);
        }
    }
}
