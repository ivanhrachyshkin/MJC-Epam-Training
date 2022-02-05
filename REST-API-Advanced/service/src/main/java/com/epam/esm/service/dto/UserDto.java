package com.epam.esm.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {

    private Integer id;
    private String email;
    private Set<OrderDto> orders = new HashSet<>();
}
