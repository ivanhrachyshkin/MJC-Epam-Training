package com.epam.esm.service.validator;

import com.epam.esm.service.DummyRb;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    private static final Map<String, Object> messages = new HashMap<String, Object>() {{
        put("validator.order.giftId.required", "GiftCertificate name is required");
        put("validator.order.giftId.negative", "GiftCertificate name is empty");
        put("validator.order.userId.required", "GiftCertificate description is required");
        put("validator.order.userId.negative", "GiftCertificate description is empty");
    }};

    private final DummyRb dummyRb = new DummyRb();
    private OrderValidator orderValidator = new OrderValidator();

    @BeforeEach
    void setUp() {
        orderValidator.setRb(dummyRb);
        ReflectionTestUtils.setField(orderValidator, "rb", dummyRb);
        dummyRb.setMessages(messages);
    }

    static Stream<Arguments> createOrderValidatorDataProvider() {
        return Stream.of(
                Arguments.arguments(messages.get("validator.order.userId.required"), null, null),
                Arguments.arguments(messages.get("validator.order.userId.negative"), -1, null),
                Arguments.arguments(messages.get("validator.order.giftId.required"), 1, null),
                Arguments.arguments(messages.get("validator.order.giftId.negative"), 1, -1)
        );
    }

    @ParameterizedTest
    @MethodSource("createOrderValidatorDataProvider")
    void shouldThrowException_On_CreateGiftCertificateValidator(
            final String expected,
            final Integer userId,
            final Integer giftId) {
        //Given
        final OrderDto orderDto = getOrderDto(userId, giftId);
        //When
        final ValidationException validationException
                = assertThrows(ValidationException.class,
                () -> orderValidator.createValidate(orderDto));
        //Then
        assertEquals(expected, validationException.getMessage());
    }

    private OrderDto getOrderDto(final Integer userId,
                                 final Integer giftCertificateId) {
        final UserDto userDto = new UserDto(userId);
        final GiftCertificateDto giftCertificateDto = new GiftCertificateDto(giftCertificateId);
        final OrderDto orderDto = new OrderDto();
        orderDto.setUserDto(userDto);
        orderDto.setGiftCertificateDto(giftCertificateDto);
        return orderDto;
    }
}