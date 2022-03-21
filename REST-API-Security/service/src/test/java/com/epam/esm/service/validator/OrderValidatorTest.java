package com.epam.esm.service.validator;

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
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private ExceptionStatusPostfixProperties properties;
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

    static Stream<Arguments> createOrderValidatorDataProviderAdminRole() throws IOException {
        return Stream.of(
                Arguments.arguments(getRb().getString("validator.id.should.not.passed"), 1, 1, 1),
                Arguments.arguments(getRb().getString("validator.order.giftId.required"), null, 1, null),
                Arguments.arguments(getRb().getString("validator.order.giftId.negative"), null, 1, -1),
                Arguments.arguments(getRb().getString("validator.order.userId.required"), null, null, 1),
                Arguments.arguments(getRb().getString("validator.order.userId.negative"), null, -1, 1)
        );
    }

    static Stream<Arguments> createOrderValidatorDataProviderUserRole() throws IOException {
        return Stream.of(
                Arguments.arguments(getRb().getString("validator.id.should.not.passed"), 1, 1, 1),
                Arguments.arguments(getRb().getString("validator.order.giftId.required"), null, null, null),
                Arguments.arguments(getRb().getString("validator.order.giftId.negative"), null, null, -1),
                Arguments.arguments(getRb().getString("validator.order.userId.should.not.pass"), null, 1, 1)
        );
    }

    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(orderValidator, "rb", getRb());
    }

    @Test
    void shouldThrowException_On_ValidateId() throws IOException {
        //Given
        final int id = -1;
        //When
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> orderValidator.validateId(id));
        //Then
        assertEquals(getRb().getString("validator.id.non"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_ForNull() throws IOException {
        //Given
        dtoOrder = null;
        //When
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> orderValidator.createValidate(dtoOrder, true));
        //Then
        assertEquals(getRb().getString("validator.order.null.value"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_ForNullGiftCertificates() throws IOException {
        //Given
        when(dtoOrder.getId()).thenReturn(null);
        when(dtoOrder.getDtoGiftCertificates()).thenReturn(null);
        //When
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> orderValidator.createValidate(dtoOrder, true));
        //Then
        assertEquals(getRb().getString("validator.order.giftId.required"), validationException.getMessage());
    }

    @Test
    void shouldThrowException_ForEmptylGiftCertificates() throws IOException {
        //Given
        when(dtoOrder.getId()).thenReturn(null);
        when(dtoOrder.getDtoGiftCertificates()).thenReturn(Collections.EMPTY_SET);
        //When
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> orderValidator.createValidate(dtoOrder, true));
        //Then
        assertEquals(getRb().getString("validator.order.giftId.required"), validationException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("createOrderValidatorDataProviderAdminRole")
    void shouldThrowException_On_CreateGiftCertificate_ForAdmin(
            final String expected,
            final Integer orderId,
            final Integer userId,
            final Integer giftId) {
        //Given
        final OrderDto orderDto = getOrderDto(orderId, userId, giftId);
        //When
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> orderValidator.createValidate(orderDto, true));
        //Then
        assertEquals(expected, validationException.getMessage());
    }

    @ParameterizedTest
    @MethodSource("createOrderValidatorDataProviderUserRole")
    void shouldThrowException_On_CreateGiftCertificate_ForUser(
            final String expected,
            final Integer orderId,
            final Integer userId,
            final Integer giftId) {
        //Given
        final OrderDto orderDto = getOrderDto(orderId, userId, giftId);
        //When
        final ValidationException validationException
                = assertThrows(
                ValidationException.class, () -> orderValidator.createValidate(orderDto, false));
        //Then
        assertEquals(expected, validationException.getMessage());
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
