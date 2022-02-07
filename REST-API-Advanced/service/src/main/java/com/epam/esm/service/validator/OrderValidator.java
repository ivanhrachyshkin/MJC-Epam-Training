package com.epam.esm.service.validator;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    private final ResourceBundle rb;

    public void createValidate(final OrderDto orderDto) {

        if (orderDto.getUserDto().getId() == null) {
            throw new ValidationException(rb.getString("validator.order.userId.required"));
        }

        if (orderDto.getUserDto().getId() <= 0) {
            throw new ValidationException(rb.getString("validator.order.userId.negative"));
        }

        if (orderDto.getGiftCertificateDto().getId() == null) {
            throw new ValidationException(rb.getString("validator.order.giftId.required"));
        }

        if (orderDto.getGiftCertificateDto().getId() <= 0) {
            throw new ValidationException(rb.getString("validator.order.giftId.negative"));
        }
    }
}
