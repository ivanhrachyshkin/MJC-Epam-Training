package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class GiftCertificateValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;
    private final TagValidator tagValidator;

    public void updateValidate(final GiftCertificateDto giftCertificateDto) {

        if (giftCertificateDto.getId() != null) {
            throw new ValidationException(
                    rb.getString("id.value.passed"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (giftCertificateDto.getName() != null && giftCertificateDto.getName().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.name.empty"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (giftCertificateDto.getDescription() != null && giftCertificateDto.getDescription().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.description.empty"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (giftCertificateDto.getPrice() != null && giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.price.negative"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (giftCertificateDto.getDuration() != null && giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.duration.negative"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }
    }

    public void createValidate(final GiftCertificateDto giftCertificateDto) {

        if (giftCertificateDto.getId() != null) {
            throw new ValidationException(
                    rb.getString("id.value.passed"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (giftCertificateDto.getName() == null || giftCertificateDto.getName().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.name.required"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (giftCertificateDto.getDescription() == null || giftCertificateDto.getDescription().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.description.required"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (giftCertificateDto.getPrice() == null) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.price.required"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (giftCertificateDto.getPrice() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.price.negative"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (giftCertificateDto.getDuration() == null) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.duration.required"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (giftCertificateDto.getDuration() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.duration.negative"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (giftCertificateDto.getDtoTags() != null) {
            giftCertificateDto.getDtoTags().forEach(tagValidator::createValidate);
        }
    }


    public void readAllValidate(final List<String> tags,
                                final GiftCertificateRequestParamsContainer container) {

        if (tags != null && !tags.isEmpty()) {
            tags.forEach(tag -> {
                if (tag == null || tag.isEmpty()) {
                    throw new ValidationException(
                            rb.getString("validator.tag.name.empty"),
                            HttpStatus.BAD_REQUEST, properties.getGift());
                }
            });
        }

        if (container.getName() != null && container.getName().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.name.empty"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        if (container.getDescription() != null && container.getDescription().isEmpty()) {
            throw new ValidationException(
                    rb.getString("validator.giftCertificate.description.empty"),
                    HttpStatus.BAD_REQUEST, properties.getGift());
        }

        sortValidate(container.getNameSort());
        sortValidate(container.getDateSort());
    }


    private void sortValidate(final String sort) {

        if (sort != null && sort.isEmpty()) {
            throw new ValidationException(rb.getString("sort.empty"),
                    HttpStatus.BAD_REQUEST, properties.getOrder());
        }

        if (sort != null
                && !sort.equalsIgnoreCase("ASC")
                && !sort.equalsIgnoreCase("DESC")) {
            throw new ValidationException(rb.getString("invalid.value"),
                    HttpStatus.BAD_REQUEST, properties.getOrder());
        }
    }
}
