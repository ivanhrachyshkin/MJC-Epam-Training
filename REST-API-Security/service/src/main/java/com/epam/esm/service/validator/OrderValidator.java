package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.OrderDto;
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

    public void createValidate(final OrderDto orderDto) {

        if (orderDto.getId() == null) {
            throw new ValidationException(
                    rb.getString("validator.order.null.value"),
                    HttpStatus.BAD_REQUEST, properties.getOrder());
        }

        if (orderDto.getId() != null) {
            throw new ValidationException(
                    rb.getString("id.value.passed"),
                    HttpStatus.BAD_REQUEST, properties.getOrder());
        }

        if (orderDto.getUserDto() == null || orderDto.getUserDto().getId() == null) {
            throw new ValidationException(
                    rb.getString("validator.order.userId.required"),
                    HttpStatus.BAD_REQUEST, properties.getOrder());
        }

        if (orderDto.getUserDto().getId() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.order.userId.negative"),
                    HttpStatus.BAD_REQUEST, properties.getOrder());
        }

        if (orderDto.getGiftCertificateDto() == null || orderDto.getGiftCertificateDto().getId() == null) {
            throw new ValidationException(
                    rb.getString("validator.order.giftId.required"),
                    HttpStatus.BAD_REQUEST, properties.getOrder());
        }

        if (orderDto.getGiftCertificateDto().getId() <= 0) {
            throw new ValidationException(
                    rb.getString("validator.order.giftId.negative"),
                    HttpStatus.BAD_REQUEST, properties.getOrder());
        }
    }

    public void validateId(final int id) {
        if (id < 1) {
            throw new ValidationException(rb.getString("id.non"),
                    HttpStatus.BAD_REQUEST, properties.getOrder());
        }
    }
}
