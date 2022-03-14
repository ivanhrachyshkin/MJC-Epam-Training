package com.epam.esm.controller;


import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.controller.security.JwtUserDetailsService;
import com.epam.esm.controller.security.jwt.JwtUser;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
    private final JwtUserDetailsService userDetailsService;

    @Secured({USER, ADMIN})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> create(@RequestBody final OrderDto orderDto, final Principal principal) {
        final Integer userId = getPrincipalId(principal);
        final OrderDto createdOrderDto = orderService.create(orderDto, userId);
        hateoasCreator.linkOrderDtoOne(createdOrderDto);
        hateoasCreator.linkUserDto(createdOrderDto.getUserDto());
        hateoasCreator.linkGiftCertificateDto(createdOrderDto.getGiftCertificateDto());
        final HttpHeaders httpHeaders = setLocationHeader(createdOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(createdOrderDto);
    }

    @Secured({USER, ADMIN})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<OrderDto> readAll(@PageableDefault(page = 0, size = 10) final Pageable pageable,
                                        final Principal principal) {
        final Integer userId = getPrincipalId(principal);
        final Page<OrderDto> dtoOrders = orderService.readAll(pageable, userId);
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

    private HttpHeaders setLocationHeader(final OrderDto orderDto) {
        final String href = linkTo(methodOn(OrderController.class)
                .readOne(orderDto.getId()))
                .withSelfRel().getHref();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.LOCATION, href);
        return httpHeaders;
    }

    private Integer getPrincipalId (final Principal principal) {
        final String name = principal.getName();
        final JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(name);
        return userDetails.getId();
    }
}
