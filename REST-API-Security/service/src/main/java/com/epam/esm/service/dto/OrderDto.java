package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto extends RepresentationModel<OrderDto> {

    private Integer id;
    private Float price;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime date;
    private UserDto userDto;
    private Set<GiftCertificateDto> dtoGiftCertificates;

    public OrderDto(final Integer id) {
        this.id = id;
    }

    public OrderDto(final UserDto userDto, final Set<GiftCertificateDto> dtoGiftCertificates) {
        this.userDto = userDto;
        this.dtoGiftCertificates = dtoGiftCertificates;
    }
}
