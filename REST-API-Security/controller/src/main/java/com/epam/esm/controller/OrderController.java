package com.epam.esm.controller;


import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;
import static com.epam.esm.service.dto.RoleDto.Roles.USER;

@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {

    private final HateoasCreator hateoasCreator;
    private final OrderService orderService;

    @Secured({USER,ADMIN})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto create(@RequestBody OrderDto orderDto) {
        final OrderDto createdOrderDto = orderService.create(orderDto);
        hateoasCreator.linkOrderDtoOne(createdOrderDto);
        hateoasCreator.linkUserDto(createdOrderDto.getUserDto());
        hateoasCreator.linkGiftCertificateDto(createdOrderDto.getGiftCertificateDto());
        return createdOrderDto;
    }

    @Secured({USER,ADMIN})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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

    @Secured({USER,ADMIN})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping(value = "/{id}")
    public HttpEntity<OrderDto> readOne(@PathVariable final int id) {
        final OrderDto orderDto = orderService.readOne(id);
        hateoasCreator.linkOrderDtoOne(orderDto);
        hateoasCreator.linkUserDto(orderDto.getUserDto());
        hateoasCreator.linkGiftCertificateDto(orderDto.getGiftCertificateDto());
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }
}
