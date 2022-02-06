package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends RepresentationModel<UserDto> {

    private Integer id;
    private String email;
    private Set<OrderDto> dtoOrders;

    public UserDto(Integer id) {
        this.id = id;
    }
}
