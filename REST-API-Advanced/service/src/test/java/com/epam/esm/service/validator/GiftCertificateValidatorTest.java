package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateValidatorTest {

    @Mock
    private TagValidator tagValidator;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private GiftCertificateValidator giftCertificateValidator;

    private static ResourceBundle getRb() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        return new PropertyResourceBundle(contentStream);
    }

    static Stream<Arguments> createGiftCertificateValidatorDataProvider() throws IOException {
        return Stream.of(
                Arguments.arguments(getRb().getString("id.value.passed"), 1,
                        null, StringUtils.EMPTY, null, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.name.required"), null,
                        null, StringUtils.EMPTY, null, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.name.required"), null,
                        StringUtils.EMPTY, StringUtils.EMPTY, null, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.description.required"), null,
                        "name", null, null, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.description.required"), null,
                        "name", StringUtils.EMPTY, null, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.price.required"), null,
                        "name", "desc", null, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.price.negative"), null,
                        "name", "desc", -1.0F, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.duration.required"), null,
                        "name", "desc", 1.0F, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.duration.negative"), null,
                        "name", "desc", 1.0F, -1)
        );
    }

    static Stream<Arguments> updateGiftCertificateValidatorDataProvider() throws IOException {
        return Stream.of(
                Arguments.arguments(getRb().getString("id.value.passed"), 1,
                        null, StringUtils.EMPTY, null, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.name.empty"), null,
                        StringUtils.EMPTY, StringUtils.EMPTY, null, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.description.empty"), null,
                        "name", StringUtils.EMPTY, null, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.price.negative"), null,
                        "name", "desc", -1.0F, null),
                Arguments.arguments(getRb().getString("validator.giftCertificate.duration.negative"), null,
                        "name", "desc", 1.0F, -1)
        );
    }

    static Stream<Arguments> readAllGiftCertificateValidatorDataProvider() throws IOException {
        return Stream.of(
                Arguments.arguments(
                        getRb().getString("validator.tag.name.empty"),
                        Collections.singletonList(StringUtils.EMPTY),
                        new GiftCertificateRequestParamsContainer(
                                null, null, null, null)),
                Arguments.arguments(
                        getRb().getString("validator.giftCertificate.name.empty"),
                        Collections.singletonList("tagName"),
                        new GiftCertificateRequestParamsContainer(
                                StringUtils.EMPTY, null, null, null)),
                Arguments.arguments(
                        getRb().getString("validator.giftCertificate.description.empty"),
                        Collections.singletonList("tagName"),
                        new GiftCertificateRequestParamsContainer(
                                "aa", StringUtils.EMPTY, null, null)),
                Arguments.arguments(
                        getRb().getString("sort.empty"),
                        Collections.singletonList("tagName"),
                        new GiftCertificateRequestParamsContainer(
                                "aa", "dd", StringUtils.EMPTY, null)),
                Arguments.arguments(
                        getRb().getString("invalid.value"),
                        Collections.singletonList("tagName"),
                        new GiftCertificateRequestParamsContainer(
                                "aa", "dd", "asc", "aa"))
        );
    }

    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(giftCertificateValidator, "rb", getRb());
    }

    @ParameterizedTest
    @MethodSource("createGiftCertificateValidatorDataProvider")
    void shouldThrowException_On_CreateGiftCertificateValidator(
            final String expected,
            final Integer id,
            final String name,
            final String description,
            final Float price,
            final Integer duration
    ) {
        //Given
        final GiftCertificateDto giftCertificateDto = getGiftCertificateDto(id, name, description, price, duration);
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
            final Integer id,
            final String name,
            final String description,
            final Float price,
            final Integer duration
    ) {
        //Given
        final GiftCertificateDto giftCertificateDto = getGiftCertificateDto(id, name, description, price, duration);
        //When
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> giftCertificateValidator.updateValidate(giftCertificateDto));
        //Then
        assertEquals(expected, validationException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("readAllGiftCertificateValidatorDataProvider")
    void shouldThrowException_On_ReadAllGiftCertificateValidator(final String expected,
                                                                 final List<String> tags,
                                                                 final GiftCertificateRequestParamsContainer container) {
        //When
        final ValidationException validationException
                = assertThrows(
                ValidationException.class,
                () -> giftCertificateValidator.readAllValidate(tags, container));
        //Then
        assertEquals(expected, validationException.getMessage());
    }

    private GiftCertificateDto getGiftCertificateDto(final Integer id,
                                                     final String name,
                                                     final String description,
                                                     final Float price,
                                                     final Integer duration) {
        final GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setId(id);
        giftCertificateDto.setName(name);
        giftCertificateDto.setDescription(description);
        giftCertificateDto.setPrice(price);
        giftCertificateDto.setDuration(duration);
        return giftCertificateDto;
    }
}