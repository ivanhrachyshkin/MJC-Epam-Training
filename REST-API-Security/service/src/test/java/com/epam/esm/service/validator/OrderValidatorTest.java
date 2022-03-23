package com.epam.esm.service.validator;

import com.epam.esm.service.AssertionsProvider;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest extends AssertionsProvider<OrderDto> {

    @InjectMocks
    private OrderValidator orderValidator;
    @Mock
    private OrderDto dtoOrder;

    private static ResourceBundle getRb() throws IOException {
        final InputStream contentStream
                = Thread.currentThread().getContextClassLoader().getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        return new PropertyResourceBundle(contentStream);
    }

    private static ExceptionStatusPostfixProperties getProperties() {
        return new ExceptionStatusPostfixProperties();
    }

    private static ValidationException getException(final String message) {
        return new ValidationException(message, HttpStatus.BAD_REQUEST, getProperties().getGift());
    }

    static Stream<Arguments> createOrderValidatorDataProviderAdminRole() throws IOException {
        final ResourceBundle rb = getRb();
        return Stream.of(
                Arguments.arguments(
                        getException(rb.getString("validator.id.should.not.passed")),
                        1, 1, 1),
                Arguments.arguments(
                        getException(rb.getString("validator.order.giftId.required")),
                        null, 1, null),
                Arguments.arguments(
                        getException(rb.getString("validator.order.giftId.negative")),
                        null, 1, -1),
                Arguments.arguments(
                        getException(rb.getString("validator.order.userId.required")),
                        null, null, 1),
                Arguments.arguments(
                        getException(rb.getString("validator.order.userId.negative")),
                        null, -1, 1)
        );
    }

    static Stream<Arguments> createOrderValidatorDataProviderUserRole() throws IOException {
        final ResourceBundle rb = getRb();
        return Stream.of(
                Arguments.arguments(
                        getException(rb.getString("validator.id.should.not.passed")),
                        1, 1, 1),
                Arguments.arguments(
                        getException(rb.getString("validator.order.giftId.required")),
                        null, null, null),
                Arguments.arguments(
                        getException(rb.getString("validator.order.giftId.negative")),
                        null, null, -1),
                Arguments.arguments(
                        getException(rb.getString("validator.order.userId.should.not.pass")),
                        null, 1, 1)
        );
    }

    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(orderValidator, "rb", getRb());
        ReflectionTestUtils.setField(orderValidator, "properties", getProperties());
    }

    @Test
    void shouldThrowException_ForValidateId() throws IOException {
        //Given
        final ValidationException expectedException = getException(getRb().getString("validator.id.non"));
        //When
        final ValidationException actualException
                = assertThrows(ValidationException.class, () -> orderValidator.validateId(-1));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }

    @Test
    void shouldThrowException_ForNullGiftCertificates() throws IOException {
        //Given
        when(dtoOrder.getId()).thenReturn(null);
        when(dtoOrder.getDtoGiftCertificates()).thenReturn(null);
        final ValidationException expectedException
                = getException(getRb().getString("validator.order.giftId.required"));
        //When
        final ValidationException actualException
                = assertThrows(
                ValidationException.class, () -> orderValidator.createValidate(dtoOrder, true));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }

    @Test
    void shouldThrowException_ForEmptyGiftCertificates() throws IOException {
        //Given
        when(dtoOrder.getId()).thenReturn(null);
        when(dtoOrder.getDtoGiftCertificates()).thenReturn(Collections.EMPTY_SET);
        final ValidationException expectedException
                = getException(getRb().getString("validator.order.giftId.required"));
        //When
        final ValidationException actualException
                = assertThrows(
                ValidationException.class, () -> orderValidator.createValidate(dtoOrder, true));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }

    @ParameterizedTest
    @MethodSource("createOrderValidatorDataProviderAdminRole")
    void shouldThrowException_On_CreateGiftCertificate_ForAdmin(
            final ValidationException expectedException,
            final Integer orderId,
            final Integer userId,
            final Integer giftId) {
        //Given
        final OrderDto orderDto = getOrderDto(orderId, userId, giftId);
        //When
        final ValidationException actualException
                = assertThrows(
                ValidationException.class, () -> orderValidator.createValidate(orderDto, true));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }

    @ParameterizedTest
    @MethodSource("createOrderValidatorDataProviderUserRole")
    void shouldThrowException_On_CreateGiftCertificate_ForUser(
            final ValidationException expectedException,
            final Integer orderId,
            final Integer userId,
            final Integer giftId) {
        //Given
        final OrderDto orderDto = getOrderDto(orderId, userId, giftId);
        //When
        final ValidationException actualException
                = assertThrows(
                ValidationException.class, () -> orderValidator.createValidate(orderDto, false));
        //Then
        assertValidationExceptions(expectedException, actualException);
    }


    private OrderDto getOrderDto(final Integer orderId,
                                 final Integer userId,
                                 final Integer giftCertificateId) {
        final UserDto userDto = new UserDto(userId);
        final GiftCertificateDto giftCertificateDto = new GiftCertificateDto(giftCertificateId);
        final OrderDto orderDto = new OrderDto();
        orderDto.setId(orderId);
        orderDto.setUserDto(userDto);
        orderDto.setDtoGiftCertificates(Collections.singleton(giftCertificateDto));
        return orderDto;
    }
}
