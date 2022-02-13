package com.epam.esm.controller;


import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    public ResponseEntity<EntityModel<OrderDto>> create(@RequestBody OrderDto orderDto) {
        final OrderDto createdOrderDto = orderService.create(orderDto);
        final EntityModel<OrderDto> orderDtoEntityModel = linkOrderDtoHal(createdOrderDto);
        return new ResponseEntity<>(orderDtoEntityModel, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<OrderDto>> readAll(@RequestParam(required = false) final Integer userId) {
        final List<OrderDto> dtoOrders = orderService.readAll(userId);
        dtoOrders.forEach(this::linkOrderDto);
        return new ResponseEntity<>(dtoOrders, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    public ResponseEntity<EntityModel<OrderDto>> readOne(@PathVariable final int id) {
        final OrderDto orderDto = orderService.readOne(id);
        final EntityModel<OrderDto> orderDtoEntityModel = linkOrderDtoHal(orderDto);
        return new ResponseEntity<>(orderDtoEntityModel, HttpStatus.OK);
    }

    private EntityModel<OrderDto> linkOrderDtoHal(final OrderDto orderDto) {
        linkUserDto(orderDto.getUserDto());
        linkGiftCertificateDto(orderDto.getGiftCertificateDto());

        return EntityModel.of(orderDto,
                linkTo(methodOn(OrderController.class).readOne(orderDto.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(OrderController.class).create(null))));
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
