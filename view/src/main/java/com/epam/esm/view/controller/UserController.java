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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final HttpServletResponse response;
    private final HateoasCreator hateoasCreator;
    private final UserService userService;
    private final OrderService orderService;

    @Profile("jwt")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody final UserDto userDto) {
        final UserDto createdUserDto = userService.create(userDto);
        hateoasCreator.linkUserDto(createdUserDto);
        createdUserDto.getDtoOrders().forEach(hateoasCreator::linkOrderDto);
        setLocationHeader(createdUserDto);
        return createdUserDto;
    }

    @Secured({ADMIN})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<UserDto> readAll(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<UserDto> dtoUsers = userService.readAll(pageable);
        dtoUsers.forEach(userDto -> userDto.getDtoOrders().forEach(hateoasCreator::linkOrderDto));
        return hateoasCreator.linkUserDtos(dtoUsers);
    }

    @Secured({ADMIN})
    @GetMapping(value = "/{userId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto readOneOrderByUserIdAndOrderId(@PathVariable final int userId,
                                                   @PathVariable final int orderId) {
        final OrderDto orderDto = orderService.readOneByUserIdAndOrderId(userId, orderId);
        hateoasCreator.linkOrderDtoOne(orderDto);
        hateoasCreator.linkUserDto(orderDto.getUserDto());
        orderDto.getDtoGiftCertificates().forEach(hateoasCreator::linkGiftCertificateDto);
        return orderDto;
    }

    @Secured({ADMIN})
    @GetMapping(value = "/{userId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<OrderDto> readOrdersByUserId(@PathVariable final int userId,
                                                   @PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<OrderDto> dtoOrders = orderService.readByUserId(userId, pageable);
        dtoOrders.forEach(orderDto -> {
            hateoasCreator.linkUserDto(orderDto.getUserDto());
            orderDto
                    .getDtoGiftCertificates()
                    .forEach(hateoasCreator::linkGiftCertificateDto);
        });
        return hateoasCreator.linkOrderDtos(dtoOrders);
    }

    @Secured({ADMIN})
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto readOne(@PathVariable final int id) {
        final UserDto userDto = userService.readOne(id);
        hateoasCreator.linkUserDto(userDto);
        userDto.getDtoOrders().forEach(hateoasCreator::linkOrderDto);
        return userDto;
    }

    private void setLocationHeader(final UserDto userDto) {
        final String href = linkTo(methodOn(UserController.class)
                .readOne(userDto.getId()))
                .withSelfRel().getHref();
        response.addHeader(HttpHeaders.LOCATION, href);
    }
}
