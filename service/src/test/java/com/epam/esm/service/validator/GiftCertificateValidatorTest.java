package com.epam.esm.service.validator;

import com.epam.esm.service.AssertionsProvider;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import com.epam.esm.service.dto.TagDto;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GiftCertificateValidatorTest extends AssertionsProvider<GiftCertificateDto> {

    @Mock
    private TagValidator tagValidator;
    @InjectMocks
    private GiftCertificateValidator giftCertificateValidator;

    private static ResourceBundle getRb() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        return new PropertyResourceBundle(contentStream);
    }

    private static ExceptionStatusPostfixProperties getProperties() {
        return new ExceptionStatusPostfixProperties();
    }

    private static ValidationException getException(final String message) {
        return new ValidationException(message, HttpStatus.BAD_REQUEST, getProperties().getGift());
    }

    static Stream<Arguments> createGiftCertificateValidatorDataProvider() throws IOException {
        final ResourceBundle rb = getRb();
        return Stream.of(
                Arguments.arguments(
                        getException(rb.getString("validator.id.should.not.passed")),
                        1, StringUtils.EMPTY, StringUtils.EMPTY, null, null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.name.required")),
                        null, StringUtils.EMPTY, StringUtils.EMPTY, null, null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.name.required")),
                        null, StringUtils.EMPTY, StringUtils.EMPTY, null, null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.description.required")),
                        null, "name", null, null, null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.description.required")),
                        null, "name", StringUtils.EMPTY, null, null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.price.required")),
                        null, "name", "desc", null, null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.duration.required")),
                        null, "name", "desc", 1.0F, null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.price.negative")),
                        null, "name", "desc", -10.0F, 11, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.duration.negative")),
                        null, "name", "desc", 1.0F, -1, null, null, null),
                Arguments.arguments(
                        getException(getRb().getString("validator.date.should.not.passed")),
                        null, "name", "desc", 1.0F, 1, LocalDateTime.now(), null, null),
                Arguments.arguments(
                        getException(getRb().getString("validator.date.should.not.passed")),
                        null, "name", "desc", 1.0F, 1, null, LocalDateTime.now(), null),
                Arguments.arguments(
                        getException(getRb().getString("validator.active.should.not.passed")),
                        null, "name", "desc", 1.0F, 1, null, null, true)
        );
    }

    static Stream<Arguments> updateGiftCertificateValidatorDataProvider() throws IOException {
        final ResourceBundle rb = getRb();
        return Stream.of(
                Arguments.arguments(
                        getException(rb.getString("validator.id.should.passed")),
                        null, "name", "desc", null, null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.name.required")),
                        1, StringUtils.EMPTY, null, null, null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.description.required")),
                        1, null, StringUtils.EMPTY, null, null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.price.negative")),
                        1, null, null, -10.0F, null, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.duration.negative")),
                        1, null, null, null, -1, null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.date.should.not.passed")),
                        1, null, null, null, null, LocalDateTime.now(), null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.date.should.not.passed")),
                        1, "name", "desc", 1.0F, 1, null, LocalDateTime.now(), null),
                Arguments.arguments(
                        getException(rb.getString("validator.active.should.not.passed")),
                        1, "name", "desc", 1.0F, 1, null, null, true)
        );
    }

    static Stream<Arguments> readAllGiftCertificateValidatorDataProvider() throws IOException {
        final ResourceBundle rb = getRb();
        return Stream.of(
                Arguments.arguments(
                        getException(rb.getString("validator.tag.name.empty")),
                        Collections.emptyList(),
                        new GiftCertificateRequestParamsContainer(null, null)),
                Arguments.arguments(
                        getException(rb.getString("validator.tag.name.empty")),
                        Collections.singletonList(StringUtils.EMPTY),
                        new GiftCertificateRequestParamsContainer(null, null)),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.name.empty")),
                        Collections.singletonList("tagName"),
                        new GiftCertificateRequestParamsContainer(StringUtils.EMPTY, null)),
                Arguments.arguments(
                        getException(rb.getString("validator.giftCertificate.description.empty")),
                        Collections.singletonList("tagName"),
                        new GiftCertificateRequestParamsContainer("name", StringUtils.EMPTY))
        );
    }

    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(giftCertificateValidator, "rb", getRb());
        ReflectionTestUtils.setField(giftCertificateValidator, "properties", getProperties());
    }

    @Test
    void shouldThrowException_ForValidateId() throws IOException {
        //Given
        final ValidationException expectedException = getException(getRb().getString("validator.id.non"));
        //When
        final ValidationException actualException
                = assertThrows(ValidationException.class, () -> giftCertificateValidator.validateId(-1));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }

    @ParameterizedTest
    @MethodSource("createGiftCertificateValidatorDataProvider")
    void shouldThrowException_On_CreateGiftCertificateValidator(final ValidationException expectedException,
                                                                final Integer id,
                                                                final String name,
                                                                final String description,
                                                                final Float price,
                                                                final Integer duration,
                                                                final LocalDateTime createDate,
                                                                final LocalDateTime lastUpdate,
                                                                final Boolean active) {
        //Given
        final GiftCertificateDto giftCertificateDto
                = getGiftCertificateDto(id, name, description, price, duration, createDate, lastUpdate, active);
        //When
        final ValidationException actualException
                = assertThrows(ValidationException.class,
                () -> giftCertificateValidator.validateCreateOrUpdate(giftCertificateDto, HttpMethod.POST.name()));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }

    @ParameterizedTest
    @MethodSource("updateGiftCertificateValidatorDataProvider")
    void shouldThrowException_On_UpdateGiftCertificateValidator(final ValidationException expectedException,
                                                                final Integer id,
                                                                final String name,
                                                                final String description,
                                                                final Float price,
                                                                final Integer duration,
                                                                final LocalDateTime createDate,
                                                                final LocalDateTime lastUpdate,
                                                                final Boolean active) {
        //Given
        final GiftCertificateDto giftCertificateDto
                = getGiftCertificateDto(id, name, description, price, duration, createDate, lastUpdate, active);
        //When
        final ValidationException actualException
                = assertThrows(ValidationException.class,
                () -> giftCertificateValidator.validateCreateOrUpdate(giftCertificateDto, HttpMethod.PATCH.name()));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }

    @ParameterizedTest
    @MethodSource("readAllGiftCertificateValidatorDataProvider")
    void shouldThrowException_On_ReadAllGiftCertificateValidator(final ValidationException expectedException,
                                                                 final List<String> tags,
                                                                 final GiftCertificateRequestParamsContainer container) {
        //When
        final ValidationException actualException
                = assertThrows(ValidationException.class,
                () -> giftCertificateValidator.readAllValidate(tags, container));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }

    private GiftCertificateDto getGiftCertificateDto(final Integer id,
                                                     final String name,
                                                     final String description,
                                                     final Float price,
                                                     final Integer duration,
                                                     final LocalDateTime createDate,
                                                     final LocalDateTime lastUpdateDate,
                                                     final Boolean active) {
        final GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setId(id);
        giftCertificateDto.setName(name);
        giftCertificateDto.setDescription(description);
        giftCertificateDto.setPrice(price);
        giftCertificateDto.setDuration(duration);
        giftCertificateDto.setCreateDate(createDate);
        giftCertificateDto.setLastUpdateDate(lastUpdateDate);
        giftCertificateDto.setLastUpdateDate(lastUpdateDate);
        giftCertificateDto.setActive(active);
        giftCertificateDto.setDtoTags(Collections.singleton(new TagDto()));
        return giftCertificateDto;
    }


}
