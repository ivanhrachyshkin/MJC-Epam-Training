package com.epam.esm.controller;


import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;
import static com.epam.esm.service.dto.RoleDto.Roles.USER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {

    private final HateoasCreator hateoasCreator;
    private final OrderService orderService;

    @Profile("jwt")
    @Secured({USER, ADMIN})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> create(@RequestBody final OrderDto orderDto) {
        final OrderDto createdOrderDto = orderService.create(orderDto);
        hateoasCreator.linkOrderDtoOne(createdOrderDto);
        hateoasCreator.linkUserDto(createdOrderDto.getUserDto());
        hateoasCreator.linkGiftCertificateDto(createdOrderDto.getGiftCertificateDto());
        final HttpHeaders httpHeaders = setLocationHeader(createdOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(createdOrderDto);
    }

    @Profile("jwt")
    @Secured({USER, ADMIN})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<OrderDto> readAll(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<OrderDto> dtoOrders = orderService.readAll(pageable);
        dtoOrders.forEach(orderDto -> {
            hateoasCreator.linkUserDto(orderDto.getUserDto());
            hateoasCreator.linkGiftCertificateDto(orderDto.getGiftCertificateDto());
        });
        return hateoasCreator.linkOrderDtos(dtoOrders);
    }

    @Secured({ADMIN})
    @GetMapping(value = "/{id}")
    public HttpEntity<OrderDto> readOne(@PathVariable final int id) {
        final OrderDto orderDto = orderService.readOne(id);
        hateoasCreator.linkOrderDtoOne(orderDto);
        hateoasCreator.linkUserDto(orderDto.getUserDto());
        hateoasCreator.linkGiftCertificateDto(orderDto.getGiftCertificateDto());
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @Secured({ADMIN})
    @GetMapping(value = "{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<OrderDto> readForUser(@PathVariable final int userId,
                                            @PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<OrderDto> dtoOrders = orderService.readAllByUserId(userId, pageable);
        dtoOrders.forEach(orderDto -> {
            hateoasCreator.linkOrderDtoOne(orderDto);
            hateoasCreator.linkGiftCertificateDto(orderDto.getGiftCertificateDto());
        });
        return hateoasCreator.linkOrderDtos(dtoOrders);
    }

    private HttpHeaders setLocationHeader(final OrderDto orderDto) {
        final String href = linkTo(methodOn(OrderController.class)
                .readOne(orderDto.getId()))
                .withSelfRel().getHref();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.LOCATION, href);
        return httpHeaders;
    }
}
