package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GiftCertificateValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;
    private final TagValidator tagValidator;

    public void validateId(final int id) {
        if (id < 1) {
            throwValidationException("id.non");
        }
    }

    public void createValidate(final GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto.getId() != null) {
            throwValidationException("id.should.not.passed");
        }
        final Map<Object, String> fieldsWithRbKeys = getFieldWithRbKey(giftCertificateDto);
        fieldsWithRbKeys.forEach(this::validateNullOrEmpty);
    }

    public void updateValidate(final GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto.getId() != null) {
            throwValidationException("id.should.passed");
        }
        final Map<Object, String> fieldsWithRbKeys = getFieldWithRbKey(giftCertificateDto);
        fieldsWithRbKeys.forEach(this::validateNotNullAndEmpty);
    }

    public void readAllValidate(final List<String> tags,
                                final GiftCertificateRequestParamsContainer container) {
        validateTags(tags);
        validateContainer(container);
    }

    private Map<Object, String> getFieldWithRbKey(final GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto == null) {
            throwValidationException("validator.giftCertificate.null");
        }
        validateNumberFields(giftCertificateDto);

        final String name = giftCertificateDto.getName();
        final String description = giftCertificateDto.getDescription();
        final Float price = giftCertificateDto.getPrice();
        final Integer duration = giftCertificateDto.getDuration();

        if (giftCertificateDto.getDtoTags() != null) {
            giftCertificateDto.getDtoTags().forEach(tagValidator::validate);
        }

        final Map<Object, String> values = new HashMap<>();
        values.put(name, "validator.giftCertificate.name.required");
        values.put(description, "validator.giftCertificate.description.required");
        values.put(price, "validator.giftCertificate.description.required");
        values.put(duration, "validator.giftCertificate.description.required");
        return values;
    }

    private void validateNullOrEmpty(final Object field, final String rbKey) {
        if (ObjectUtils.isEmpty(field)) {
            throwValidationException(rbKey);
        }
    }

    private void validateNotNullAndEmpty(final Object field, final String rbKey) {
        if (field != null && ObjectUtils.isEmpty(field)) {
            throwValidationException(rbKey);
        }
    }

    private void validateNumberFields(final GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto.getPrice() <= 0) {
            throwValidationException("validator.giftCertificate.price.negative");
        }
        if (giftCertificateDto.getDuration() <= 0) {
            throwValidationException("validator.giftCertificate.duration.negative");
        }
    }

    private void validateContainer(final GiftCertificateRequestParamsContainer container) {
        final String name = container.getName();
        final String description = container.getName();

        if (name != null && name.isEmpty()) {
            throwValidationException("validator.giftCertificate.name.empty");
        }
        if (description != null && description.isEmpty()) {
            throwValidationException("validator.giftCertificate.description.empty");
        }
    }

    private void validateTags(final List<String> tags) {
        if (tags != null && tags.isEmpty()) {
            throwValidationException("validator.tag.name.empty");
        }
        if (tags != null) {
            tags.forEach(tag -> {
                if (ObjectUtils.isEmpty(tags)) {
                    throwValidationException("validator.tag.name.empty");
                }
            });
        }
    }

    private void throwValidationException(final String rbKey) {
        throw new ValidationException(rb.getString(rbKey), HttpStatus.BAD_REQUEST, properties.getGift());
    }
}
