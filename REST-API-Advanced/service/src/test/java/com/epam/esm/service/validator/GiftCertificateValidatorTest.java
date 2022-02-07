package com.epam.esm.service.validator;

import com.epam.esm.service.DummyRb;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GiftCertificateValidatorTest {

    private static final Map<String, Object> messages = new HashMap<String, Object>() {{
        put("validator.giftCertificate.name.required", "GiftCertificate name is required");
        put("validator.giftCertificate.name.empty", "GiftCertificate name is empty");
        put("validator.giftCertificate.description.required", "GiftCertificate description is required");
        put("validator.giftCertificate.description.empty", "GiftCertificate description is empty");
        put("validator.giftCertificate.price.required", "GiftCertificate price is required");
        put("validator.giftCertificate.price.negative", "GiftCertificate price is non -positive");
        put("validator.giftCertificate.duration.required", "GiftCertificate duration is required");
        put("validator.giftCertificate.duration.negative", "GiftCertificate duration is non -positive");
        put("validator.tag.name.empty", "Tag name is empty");
    }};
    private final DummyRb dummyRb = new DummyRb();
    @Mock
    private TagValidator tagValidator;
    @InjectMocks
    private GiftCertificateValidator giftCertificateValidator;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(giftCertificateValidator, "rb", dummyRb);
        dummyRb.setMessages(messages);
    }

    static Stream<Arguments> createGiftCertificateValidatorDataProvider() {
        return Stream.of(
                Arguments.arguments(messages.get("validator.giftCertificate.name.required"),
                        null, StringUtils.EMPTY, null, null),
                Arguments.arguments(messages.get("validator.giftCertificate.name.required"),
                        StringUtils.EMPTY, StringUtils.EMPTY, null, null),
                Arguments.arguments(messages.get("validator.giftCertificate.description.required"),
                        "name", null, null, null),
                Arguments.arguments(messages.get("validator.giftCertificate.description.required"),
                        "name", StringUtils.EMPTY, null, null),
                Arguments.arguments(messages.get("validator.giftCertificate.price.required"),
                        "name", "desc", null, null),
                Arguments.arguments(messages.get("validator.giftCertificate.price.negative"),
                        "name", "desc", -1.0F, null),
                Arguments.arguments(messages.get("validator.giftCertificate.duration.required"),
                        "name", "desc", 1.0F, null),
                Arguments.arguments(messages.get("validator.giftCertificate.duration.negative"),
                        "name", "desc", 1.0F, -1)
        );
    }

    static Stream<Arguments> updateGiftCertificateValidatorDataProvider() {
        return Stream.of(
                Arguments.arguments(messages.get("validator.giftCertificate.name.empty"),
                        StringUtils.EMPTY, StringUtils.EMPTY, null, null),
                Arguments.arguments(messages.get("validator.giftCertificate.description.empty"),
                        "name", StringUtils.EMPTY, null, null),
                Arguments.arguments(messages.get("validator.giftCertificate.price.negative"),
                        "name", "desc", -1.0F, null),
                Arguments.arguments(messages.get("validator.giftCertificate.duration.negative"),
                        "name", "desc", 1.0F, -1)
        );
    }

    static Stream<Arguments> readAllGiftCertificateValidatorDataProvider() {
        return Stream.of(
                Arguments.arguments(messages.get("validator.tag.name.empty"),
                        Collections.singletonList(StringUtils.EMPTY), null, null),
                Arguments.arguments(messages.get("validator.giftCertificate.name.empty"),
                        Collections.singletonList("tagName"), StringUtils.EMPTY, null),
                Arguments.arguments(messages.get("validator.giftCertificate.description.empty"),
                        Collections.singletonList("tagName"), "name", StringUtils.EMPTY)
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
    void shouldThrowException_On_ReadAllGiftCertificateValidator(final String expected,
                                                                 final List<String> tags,
                                                                 final String name,
                                                                 final String description) {
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> giftCertificateValidator.readAllValidate(tags, name, description));
        //Then
        assertEquals(expected, validationException.getMessage());
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