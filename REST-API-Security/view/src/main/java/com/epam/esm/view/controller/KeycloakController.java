package com.epam.esm.view.controller;


import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.view.hateoas.HateoasCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;
import static com.epam.esm.service.dto.RoleDto.Roles.USER;

@Profile("keycloak")
@RestController
@RequestMapping(value = "/keycloak", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class KeycloakController {

    private final UserService userService;
    private final OrderService orderService;
    private final HateoasCreator hateoasCreator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createKeycloakUser(@RequestBody final UserDto userDto) {
        return userService.createKeycloakUser(userDto);
    }

    @Secured({USER, ADMIN})
    @GetMapping(value = "/orders")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<OrderDto> readAllKeycloak(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<OrderDto> dtoOrders = orderService.readAllKeycloak(pageable);
        dtoOrders.forEach(orderDto -> {
            hateoasCreator.linkUserDto(orderDto.getUserDto());
            orderDto
                    .getDtoGiftCertificates()
                    .forEach(hateoasCreator::linkGiftCertificateDto);
        });
        return hateoasCreator.linkOrderDtos(dtoOrders);
    }

    @Secured({USER, ADMIN})
    @GetMapping(value = "/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto readOne(@PathVariable final int id) {
        final OrderDto orderDto = orderService.readOneKeycloak(id);
        hateoasCreator.linkOrderDtoOne(orderDto);
        hateoasCreator.linkUserDto(orderDto.getUserDto());
        orderDto
                .getDtoGiftCertificates()
                .forEach(hateoasCreator::linkGiftCertificateDto);
        return orderDto;
    }
}