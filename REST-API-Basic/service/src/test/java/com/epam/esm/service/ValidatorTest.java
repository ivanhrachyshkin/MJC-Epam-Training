package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.validator.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {

    private final CreateTagValidator createTagValidator
            = new CreateTagValidator();
    private final UpdateGiftCertificateValidator updateGiftCertificateValidator
            = new UpdateGiftCertificateValidator();
    private final ReadAllGiftCertificatesValidator readAllGiftCertificatesValidator
            = new ReadAllGiftCertificatesValidator();
    private final CreateGiftCertificateValidator createGiftCertificateValidator
            = new CreateGiftCertificateValidator(createTagValidator);

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
        final GiftCertificate giftCertificate = getGiftCertificate(name, description, price, duration);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> createGiftCertificateValidator.validate(giftCertificate));
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
        final GiftCertificate giftCertificate = getGiftCertificate(name, description, price, duration);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> updateGiftCertificateValidator.validate(giftCertificate));
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
                () -> readAllGiftCertificatesValidator.validate(tag, name, description));
        //Then
        assertEquals(expected, validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_CreateTagValidator_ForEmptyName() {
        //Given
        final Tag tag = new Tag();
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class, () -> createTagValidator.validate(tag));
        //Then
        assertEquals("Tag name is required", validationException.getMessage());
    }

    @Test
    void shouldPath_On_CreateGiftCertificateValidator() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(1.0F);
        giftCertificate.setDuration(11);
        giftCertificate.setTags(new HashSet<>(Collections.singletonList(new Tag("name"))));
        //When
        createGiftCertificateValidator.validate(giftCertificate);
    }

    @Test
    void shouldPath_On_UpdateGiftCertificateValidator() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(1.0F);
        giftCertificate.setDuration(11);
        //When
        updateGiftCertificateValidator.validate(giftCertificate);
    }

    @Test
    void shouldTPath_On_ReadAllGiftCertificateValidator() {
        readAllGiftCertificatesValidator.validate("a", "a", null);
    }

    @Test
    void shouldPath_On_CreateTagValidator_ForEmptyName() {
        //Given
        final Tag tag = new Tag("name");
        //When
        createTagValidator.validate(tag);
    }

    private GiftCertificate getGiftCertificate(final String name,
                                               final String description,
                                               final Float price,
                                               final Integer duration) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(name);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(price);
        giftCertificate.setDuration(duration);
        return giftCertificate;
    }
}