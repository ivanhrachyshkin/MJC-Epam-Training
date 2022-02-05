package com.epam.esm.service.dto;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.OrderPK;
import com.epam.esm.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {

    private User user;
    private GiftCertificate giftCertificate;
    private Float price;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime date;

}
