package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GiftCertificateValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;
    private final TagValidator tagValidator;

    public void validateId(final Integer id) {
        if (id != null && id <= 0) {
            throwValidationException("validator.id.non");
        }
    }

    public void isValidateCreateElseUpdate(final GiftCertificateDto giftCertificateDto, final Boolean value) {
        validateNullOrEmpty(giftCertificateDto, "validator.giftCertificate.null");
        if (value) {
            createValidate(giftCertificateDto);
        } else {
            updateValidate(giftCertificateDto);
        }
        validateShouldNotBePassedFields(giftCertificateDto);
        validateNegativeFields(giftCertificateDto);
        validateTags(giftCertificateDto.getDtoTags());
    }

    private void createValidate(final GiftCertificateDto giftCertificateDto) {
        validateNotNullAndNotEmpty(giftCertificateDto.getId(), "validator.id.should.not.passed");
        validateNullOrEmpty(giftCertificateDto.getName(), "validator.giftCertificate.name.required");
        validateNullOrEmpty(
                giftCertificateDto.getDescription(), "validator.giftCertificate.description.required");
        validateNullOrEmpty(giftCertificateDto.getPrice(), "validator.giftCertificate.price.required");
        validateNullOrEmpty(giftCertificateDto.getDuration(), "validator.giftCertificate.duration.required");
    }

    private void updateValidate(final GiftCertificateDto giftCertificateDto) {
        validateNullOrEmpty(giftCertificateDto.getId(), "validator.id.should.passed");
        validateNotNullAndEmpty(giftCertificateDto.getName(), "validator.giftCertificate.name.required");
        validateNotNullAndEmpty(
                giftCertificateDto.getDescription(), "validator.giftCertificate.description.required");
        validateNotNullAndEmpty(giftCertificateDto.getPrice(), "validator.giftCertificate.price.required");
        validateNotNullAndEmpty(giftCertificateDto.getDuration(), "validator.giftCertificate.duration.required");
    }

    public void readAllValidate(final List<String> tags,
                                final GiftCertificateRequestParamsContainer container) {
        validateTagNames(tags);
        validateContainer(container);
    }

    private void validateNullOrEmpty(final Object field, final String rbKey) {
        if (ObjectUtils.isEmpty(field)) {
            throwValidationException(rbKey);
        }
    }

    private void validateNotNullAndNotEmpty(final Object field, final String rbKey) {
        if (!ObjectUtils.isEmpty(field)) {
            throwValidationException(rbKey);
        }
    }

    private void validateNotNullAndEmpty(final Object field, final String rbKey) {
        if (field != null && ObjectUtils.isEmpty(field)) {
            throwValidationException(rbKey);
        }
    }

    private void validateShouldNotBePassedFields(final GiftCertificateDto giftCertificateDto) {
        validateNotNullAndNotEmpty(giftCertificateDto.getCreateDate(), "validator.date.should.not.passed");
        validateNotNullAndNotEmpty(giftCertificateDto.getLastUpdateDate(), "validator.date.should.not.passed");
        validateNotNullAndNotEmpty(giftCertificateDto.getActive(), "validator.active.should.not.passed");
    }

    private void validateNegativeFields(final GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto.getPrice() != null && giftCertificateDto.getPrice() <= 0) {
            throwValidationException("validator.giftCertificate.price.negative");
        }
        if (giftCertificateDto.getDuration() != null && giftCertificateDto.getDuration() <= 0) {
            throwValidationException("validator.giftCertificate.duration.negative");
        }
    }

    private void validateTags(final Set<TagDto> dtoTags) {
        if (dtoTags!=null && !dtoTags.isEmpty()) {
            dtoTags.forEach(tagValidator::validate);
        }
    }

    private void validateTagNames(final List<String> tags) {
        if (tags != null && tags.isEmpty()) {
            throwValidationException("validator.tag.name.empty");
        }
        if (tags != null) {
            tags.forEach(tag -> {
                if (ObjectUtils.isEmpty(tag)) {
                    throwValidationException("validator.tag.name.empty");
                }
            });
        }
    }

    private void validateContainer(final GiftCertificateRequestParamsContainer container) {
        final String name = container.getName();
        final String description = container.getDescription();
        if (name != null && name.isEmpty()) {
            throwValidationException("validator.giftCertificate.name.empty");
        }
        if (description != null && description.isEmpty()) {
            throwValidationException("validator.giftCertificate.description.empty");
        }
    }

    private void throwValidationException(final String rbKey) {
        throw new ValidationException(rb.getString(rbKey), HttpStatus.BAD_REQUEST, properties.getGift());
    }
}
