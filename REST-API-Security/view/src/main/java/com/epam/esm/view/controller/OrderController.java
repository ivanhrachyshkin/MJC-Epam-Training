package com.epam.esm.view.controller;


import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.view.hateoas.HateoasCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;
import static com.epam.esm.service.dto.RoleDto.Roles.USER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Profile("jwt")
@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {

    private final HttpServletResponse response;
    private final HateoasCreator hateoasCreator;
    private final OrderService orderService;

    @Secured({USER, ADMIN})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@RequestBody final OrderDto orderDto) {
        final OrderDto createdOrderDto = orderService.create(orderDto);
        hateoasCreator.linkOrderDtoOne(createdOrderDto);
        hateoasCreator.linkUserDto(createdOrderDto.getUserDto());
        createdOrderDto
                .getDtoGiftCertificates()
                .forEach(hateoasCreator::linkGiftCertificateDto);
        setLocationHeader(createdOrderDto);
        return createdOrderDto;
    }

    @Profile("jwt")
    @Secured({USER, ADMIN})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<OrderDto> readAll(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<OrderDto> dtoOrders = orderService.readAll(pageable);
        dtoOrders.forEach(orderDto -> {
            hateoasCreator.linkUserDto(orderDto.getUserDto());
            orderDto
                    .getDtoGiftCertificates()
                    .forEach(hateoasCreator::linkGiftCertificateDto);
        });
        return hateoasCreator.linkOrderDtos(dtoOrders);
    }

    @Profile("jwt")
    @Secured({USER, ADMIN})
    @GetMapping(value = "/{id}")
    public OrderDto readOne(@PathVariable final int id) {
        final OrderDto orderDto = orderService.readOne(id);
        hateoasCreator.linkOrderDtoOne(orderDto);
        hateoasCreator.linkUserDto(orderDto.getUserDto());
        orderDto
                .getDtoGiftCertificates()
                .forEach(hateoasCreator::linkGiftCertificateDto);
        return orderDto;
    }

    private void setLocationHeader(final OrderDto orderDto) {
        final String href = linkTo(methodOn(OrderController.class)
                .readOne(orderDto.getId()))
                .withSelfRel().getHref();
        response.addHeader(HttpHeaders.LOCATION, href);
    }
}
