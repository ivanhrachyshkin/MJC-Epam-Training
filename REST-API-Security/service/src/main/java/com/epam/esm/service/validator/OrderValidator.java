package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;

    public void validateId(final Integer id) {
        if (id != null && id <= 0) {
            throwValidationException("id.non");
        }
    }

    public void createValidate(final OrderDto orderDto, final boolean isAdmin) {//todo parameter isadmin
        if (orderDto == null) {
            throwValidationException("validator.order.null.value");
        }
        if (orderDto.getId() != null) {
            throwValidationException("id.should.not.passed");
        }

        if (isAdmin) {
            validateGiftCertificateForAdmin(orderDto);
        } else {
            validateGiftCertificateForUser(orderDto);
        }
    }

    private void validateGiftCertificateForAdmin(final OrderDto orderDto) {
        final GiftCertificateDto giftCertificateDto = orderDto.getGiftCertificateDto();
        if (giftCertificateDto == null || giftCertificateDto.getId() == null) {
            throwValidationException("validator.order.giftId.required");
        }
        if (giftCertificateDto.getId() <= 0) {
            throwValidationException("validator.order.giftId.negative");
        }
    }

    private void validateGiftCertificateForUser(final OrderDto orderDto) {
        final UserDto userDto = orderDto.getUserDto();
        if (userDto == null || userDto.getId() == null) {
            throwValidationException("validator.order.userId.required");
        }
        if (orderDto.getUserDto().getId() <= 0) {
            throwValidationException("validator.order.userId.negative");
        }
    }

    private void validateOrderUserForUserRole(final OrderDto orderDto) {
        final UserDto userDto = orderDto.getUserDto();
        if (userDto != null) {
            throwValidationException("validator.order.userId.should.not.pass");
        }
    }

    private void throwValidationException(final String rbKey) {
        throw new ValidationException(rb.getString(rbKey), HttpStatus.BAD_REQUEST, properties.getOrder());
    }
}
