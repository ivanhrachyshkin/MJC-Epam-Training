package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
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
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private OrderValidator orderValidator;

    @BeforeEach
    void setUp() throws IOException {
        ReflectionTestUtils.setField(orderValidator, "rb", getRb());
    }

    static Stream<Arguments> createOrderValidatorDataProvider() throws IOException {
        return Stream.of(
                Arguments.arguments(getRb().getString("id.value.passed"), 1, null, null),
                Arguments.arguments(getRb().getString("validator.order.userId.required"), null, null, null),
                Arguments.arguments(getRb().getString("validator.order.userId.negative"), null, -1, null),
                Arguments.arguments(getRb().getString("validator.order.giftId.required"), null, 1, null),
                Arguments.arguments(getRb().getString("validator.order.giftId.negative"), null, 1, -1)
        );
    }

    @ParameterizedTest
    @MethodSource("createOrderValidatorDataProvider")
    void shouldThrowException_On_CreateGiftCertificateValidator(
            final String expected,
            final Integer orderId,
            final Integer userId,
            final Integer giftId) {
        //Given
        final OrderDto orderDto = getOrderDto(orderId, userId, giftId);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> orderValidator.createValidate(orderDto));
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
        orderDto.setGiftCertificateDto(giftCertificateDto);
        return orderDto;
    }

    private static ResourceBundle getRb() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        return new PropertyResourceBundle(contentStream);
    }
}