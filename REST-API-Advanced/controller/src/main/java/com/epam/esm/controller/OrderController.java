package com.epam.esm.controller;


import com.epam.esm.controller.hateoas.HateoasCreator;
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

    private final HateoasCreator hateoasCreator;
    private final OrderService orderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<OrderDto> create(@RequestBody OrderDto orderDto) {
        final OrderDto createdOrderDto = orderService.create(orderDto);
        hateoasCreator.linkOrderDto(createdOrderDto);
        hateoasCreator.linkUserDto(createdOrderDto.getUserDto());
        hateoasCreator.linkGiftCertificateDto(createdOrderDto.getGiftCertificateDto());
        return new ResponseEntity<>(createdOrderDto, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<OrderDto>> readAll(final Integer page, final Integer size) {
        final List<OrderDto> dtoOrders = orderService.readAll(page, size);
        dtoOrders.forEach(hateoasCreator::linkOrderDto);
        return new ResponseEntity<>(dtoOrders, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<OrderDto> readOne(@PathVariable final int id) {
        final OrderDto orderDto = orderService.readOne(id);
        hateoasCreator.linkOrderDto(orderDto);
        hateoasCreator.linkUserDto(orderDto.getUserDto());
        hateoasCreator.linkGiftCertificateDto(orderDto.getGiftCertificateDto());
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }


}
