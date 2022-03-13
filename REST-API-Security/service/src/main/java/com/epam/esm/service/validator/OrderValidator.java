package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
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

    public void createValidate(final OrderDto orderDto) {
        if (orderDto == null) {
            throwValidationException("validator.order.null.value");
        }

        if (orderDto.getId() != null) {
            throwValidationException("id.should.not.passed");
        }
        validateOrderUserAndCertificate(orderDto);
        validateNumberFields(orderDto);
    }

    private void validateOrderUserAndCertificate(final OrderDto orderDto) {
        final UserDto userDto = orderDto.getUserDto();
        final GiftCertificateDto giftCertificateDto = orderDto.getGiftCertificateDto();

        if (giftCertificateDto == null || giftCertificateDto.getId() == null) {
            throwValidationException("validator.order.giftId.required");
        }

        if (userDto == null || userDto.getId() == null) {
            throwValidationException("validator.order.userId.required");
        }
    }

    private void validateNumberFields(final OrderDto orderDto) {
        if (orderDto.getUserDto().getId() <= 0) {
            throwValidationException("validator.order.userId.negative");
        }

        if (orderDto.getGiftCertificateDto().getId() <= 0) {
            throwValidationException("validator.order.giftId.negative");
        }
    }

    private void throwValidationException(final String rbKey) {
        throw new ValidationException(rb.getString(rbKey), HttpStatus.BAD_REQUEST, properties.getOrder());
    }
}
