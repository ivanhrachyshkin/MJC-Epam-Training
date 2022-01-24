package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.validator.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

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

    //todo parametrized test

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
    void shouldPath_On_CreateTagValidator_ForEmptyName() {
        //Given
        final Tag tag = new Tag("name");
        //When
        createTagValidator.validate(tag);
    }

    @Test
    void shouldThrowException_On_UpdateGiftCertificateValidator_ForEmptyName() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("");
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> updateGiftCertificateValidator.validate(giftCertificate));
        //Then
        assertEquals("GiftCertificate name is empty", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_UpdateGiftCertificateValidator_ForEmptyDescription() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("");
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> updateGiftCertificateValidator.validate(giftCertificate));
        //Then
        assertEquals("GiftCertificate description is empty", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_UpdateGiftCertificateValidator_ForNegativePrice() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(-1.0F);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> updateGiftCertificateValidator.validate(giftCertificate));
        //Then
        assertEquals("GiftCertificate price is non-positive", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_UpdateGiftCertificateValidator_ForNegativeDuration() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(1.0F);
        giftCertificate.setDuration(-11);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> updateGiftCertificateValidator.validate(giftCertificate));
        //Then
        assertEquals("GiftCertificate duration is non-positive", validationException.getMessage());
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
    void shouldThrowException_On_CreateGiftCertificateValidator_ForNullName() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> createGiftCertificateValidator.validate(giftCertificate));
        //Then
        assertEquals("GiftCertificate name is required", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_CreateGiftCertificateValidator_ForIsEmptyDescription() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("");
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> createGiftCertificateValidator.validate(giftCertificate));
        //Then
        assertEquals("GiftCertificate description is required", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_CreateGiftCertificateValidator_ForNullPrice() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("description");
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> createGiftCertificateValidator.validate(giftCertificate));
        //Then
        assertEquals("GiftCertificate price is required", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_CreateGiftCertificateValidator_ForNegativePrice() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(-1.0F);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> createGiftCertificateValidator.validate(giftCertificate));
        //Then
        assertEquals("GiftCertificate price is non-positive", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_CreateGiftCertificateValidator_ForNullDuration() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(1.0F);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> createGiftCertificateValidator.validate(giftCertificate));
        //Then
        assertEquals("GiftCertificate duration is required", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_CreateGiftCertificateValidator_ForNegativeDuration() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(1.0F);
        giftCertificate.setDuration(-11);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> createGiftCertificateValidator.validate(giftCertificate));
        //Then
        assertEquals("GiftCertificate duration is non-positive", validationException.getMessage());
    }

    @Test
    void shouldPath_On_CreateGiftCertificateValidator() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(1.0F);
        giftCertificate.setDuration(11);
        giftCertificate.setTags(new HashSet<>(Arrays.asList(new Tag("name"))));
        //When
        createGiftCertificateValidator.validate(giftCertificate);
    }

    @Test
    void shouldThrowException_On_ReadAllGiftCertificateValidator_ForEmptyTag() {
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> readAllGiftCertificatesValidator.validate("", null, null));
        //Then
        assertEquals("Tag name is required", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_ReadAllGiftCertificateValidator_ForEmptyName() {
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> readAllGiftCertificatesValidator.validate("a", "", null));
        //Then
        assertEquals("GiftCertificate name is required", validationException.getMessage());
    }

    @Test
    void shouldThrowException_On_ReadAllGiftCertificateValidator_ForEmptyDescription() {
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> readAllGiftCertificatesValidator.validate("a", "a", ""));
        //Then
        assertEquals("GiftCertificate description is required", validationException.getMessage());
    }

    @Test
    void shouldTPath_On_ReadAllGiftCertificateValidator() {
        readAllGiftCertificatesValidator.validate("a", "a", null);
    }
}