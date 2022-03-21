package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
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
            throwValidationException("validator.id.non");
        }
    }

    public void createValidate(final OrderDto orderDto, final boolean isAdmin) {
        if (orderDto == null) {
            throwValidationException("validator.order.null.value");
        }
        if (orderDto.getId() != null) {
            throwValidationException("validator.id.should.not.passed");
        }

        validateGiftCertificate(orderDto);
        if (isAdmin) {
            validateOrderForAdminRole(orderDto);
        } else {
            validateOrderUserForUserRole(orderDto);
        }
    }

    private void validateGiftCertificate(final OrderDto orderDto) {
        if (ObjectUtils.isEmpty(orderDto.getDtoGiftCertificates())) {
            throwValidationException("validator.order.giftId.required");
        }

       orderDto.getDtoGiftCertificates().forEach(giftCertificateDto -> {
           if (giftCertificateDto == null || giftCertificateDto.getId() == null) {
               throwValidationException("validator.order.giftId.required");
           }
           if (giftCertificateDto.getId() <= 0) {
               throwValidationException("validator.order.giftId.negative");
           }
       });
    }

    private void validateOrderForAdminRole(final OrderDto orderDto) {
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
