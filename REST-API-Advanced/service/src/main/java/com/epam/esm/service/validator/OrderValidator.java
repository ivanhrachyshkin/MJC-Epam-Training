package com.epam.esm.service.validator;

import com.epam.esm.service.dto.OrderDto;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class OrderValidator {

    private static final String POSTFIX = "04";

    @Setter
    private ResourceBundle rb;

    public void createValidate(final OrderDto orderDto) {

        if(orderDto.getId() != null) {
            throw new ValidationException(
                    rb.getString("id.value.passed"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (orderDto.getUserDto().getId() == null) {
            throw new ValidationException(
                    rb.getString("validator.order.userId.required"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (orderDto.getUserDto().getId() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.order.userId.negative"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (orderDto.getGiftCertificateDto().getId() == null) {
            throw new ValidationException(
                    rb.getString("validator.order.giftId.required"), HttpStatus.BAD_REQUEST, POSTFIX);
        }

        if (orderDto.getGiftCertificateDto().getId() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.order.giftId.negative"), HttpStatus.BAD_REQUEST, POSTFIX);
        }
    }
}
