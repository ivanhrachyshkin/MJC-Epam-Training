package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.TagValidator;
import com.epam.esm.service.validator.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {

    private final TagValidator tagValidator = new TagValidator();
    private final GiftCertificateValidator giftCertificateValidator
            = new GiftCertificateValidator(tagValidator);

    static Stream<Arguments> createGiftCertificateValidatorDataProvider() {
        return Stream.of(
                Arguments.arguments("GiftCertificate name is required", null, StringUtils.EMPTY, null, null),
                Arguments.arguments("GiftCertificate name is required",
                        StringUtils.EMPTY, StringUtils.EMPTY, null, null),
                Arguments.arguments("GiftCertificate description is required", "name", null, null, null),
                Arguments.arguments("GiftCertificate description is required", "name", StringUtils.EMPTY, null, null),
                Arguments.arguments("GiftCertificate price is required", "name", "desc", null, null),
                Arguments.arguments("GiftCertificate price is non-positive", "name", "desc", -1.0F, null),
                Arguments.arguments("GiftCertificate duration is required", "name", "desc", 1.0F, null),
                Arguments.arguments("GiftCertificate duration is non-positive", "name", "desc", 1.0F, -1)
        );
    }

    static Stream<Arguments> updateGiftCertificateValidatorDataProvider() {
        return Stream.of(
                Arguments.arguments("GiftCertificate name is empty", StringUtils.EMPTY, StringUtils.EMPTY, null, null),
                Arguments.arguments("GiftCertificate description is empty", "name", StringUtils.EMPTY, null, null),
                Arguments.arguments("GiftCertificate price is non-positive", "name", "desc", -1.0F, null),
                Arguments.arguments("GiftCertificate duration is non-positive", "name", "desc", 1.0F, -1)
        );
    }

    static Stream<Arguments> readAllGiftCertificateValidatorDataProvider() {
        return Stream.of(
                Arguments.arguments("Tag name is required", StringUtils.EMPTY, null, null),
                Arguments.arguments("GiftCertificate name is required", "tagName", StringUtils.EMPTY, null),
                Arguments.arguments("GiftCertificate description is required", "name", "desc", StringUtils.EMPTY)
        );
    }

    @ParameterizedTest
    @MethodSource("createGiftCertificateValidatorDataProvider")
    void shouldThrowException_On_CreateGiftCertificateValidator(
            final String expected,
            final String name,
            final String description,
            final Float price,
            final Integer duration
    ) {
        //Given
        final GiftCertificateDto giftCertificateDto = getGiftCertificateDto(name, description, price, duration);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> giftCertificateValidator.createValidate(giftCertificateDto));
        //Then
        assertEquals(expected, validationException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("updateGiftCertificateValidatorDataProvider")
    void shouldThrowException_On_UpdateGiftCertificateValidator(
            final String expected,
            final String name,
            final String description,
            final Float price,
            final Integer duration
    ) {
        //Given
        final GiftCertificateDto giftCertificateDto = getGiftCertificateDto(name, description, price, duration);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> giftCertificateValidator.updateValidate(giftCertificateDto));
        //Then
        assertEquals(expected, validationException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("readAllGiftCertificateValidatorDataProvider")
    void shouldThrowException_On_ReadAllGiftCertificateValidator_ForEmptyDescription(final String expected,
                                                                                     final String tag,
                                                                                     final String name,
                                                                                     final String description) {
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> giftCertificateValidator.readAllValidate(tag, name, description));
        //Then
        assertEquals(expected, validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_CreateTagValidator_ForEmptyName() {
        //When
        final TagDto tagDto = new TagDto();
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> tagValidator.createValidate(tagDto));
        //Then
        assertEquals("Tag name is required", validationException.getMessage());
    }

    @Test
    void shouldPath_On_CreateGiftCertificateValidator() {
        //Given
        final GiftCertificateDto giftCertificateDto
                = getGiftCertificateDto("name", "description", 1.0F, 11);
        //When
        giftCertificateValidator.createValidate(giftCertificateDto);
    }

    @Test
    void shouldPath_On_UpdateGiftCertificateValidator() {
        //Given
        final GiftCertificateDto giftCertificateDto
                = getGiftCertificateDto("name", "description", 1.0F, 11);
        //When
        giftCertificateValidator.updateValidate(giftCertificateDto);
    }

    @Test
    void shouldTPath_On_ReadAllGiftCertificateValidator() {
        giftCertificateValidator.readAllValidate("a", "a", null);
    }

    @Test
    void shouldPath_On_CreateTagValidator_ForEmptyName() {
        //Given
        final TagDto tagDto = new TagDto();
        tagDto.setName("name");
        //When
        tagValidator.createValidate(tagDto);
    }

    private GiftCertificateDto getGiftCertificateDto(final String name,
                                                     final String description,
                                                     final Float price,
                                                     final Integer duration) {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setName(name);
        giftCertificateDto.setDescription(description);
        giftCertificateDto.setPrice(price);
        giftCertificateDto.setDuration(duration);
        return giftCertificateDto;
    }
}