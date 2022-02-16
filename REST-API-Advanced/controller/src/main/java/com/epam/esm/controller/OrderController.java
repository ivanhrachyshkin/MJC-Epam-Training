package com.epam.esm.controller;


import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;

/**
 * The intention of controller - handling of the /gift resource.
 */
@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<OrderDto> create(@RequestBody OrderDto orderDto) {
        final OrderDto createdOrderDto = orderService.create(orderDto);
        linkOrderDto(createdOrderDto);
        return new ResponseEntity<>(createdOrderDto, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<OrderDto>> readAll(final Integer page, final Integer size) {
        final List<OrderDto> dtoOrders = orderService.readAll(page, size);
        dtoOrders.forEach(this::linkOrderDto);
        return new ResponseEntity<>(dtoOrders, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<OrderDto> readOne(@PathVariable final int id) {
        final OrderDto orderDto = orderService.readOne(id);
        linkOrderDto(orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    private void linkOrderDto(final OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(OrderController.class)
                .readOne(orderDto.getId()))
                .withSelfRel());

        linkUserDto(orderDto.getUserDto());
        linkGiftCertificateDto(orderDto.getGiftCertificateDto());
    }

    private void linkGiftCertificateDto(final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .readOne(giftCertificateDto.getId()))
                .withSelfRel());
    }

    private void linkUserDto(final UserDto userDto) {
        userDto.add(linkTo(methodOn(UserController.class)
                .readOne(userDto.getId()))
                .withSelfRel());
    }

}
