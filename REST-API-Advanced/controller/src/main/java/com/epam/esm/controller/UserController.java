package com.epam.esm.controller;


import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<UserDto>> readAll() {
        final List<UserDto> dtoUsers = userService.readAll();
        dtoUsers.forEach(this::linkUserDto);
        return new ResponseEntity<>(dtoUsers, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<OrderDto>> readAllByUserId(@PathVariable int userId) {
        final List<OrderDto> dtoOrders = orderService.readAllByUserId(userId);
        return new ResponseEntity<>(dtoOrders, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/orders/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<OrderDto> readAllByUserIdAndOrderId(@PathVariable int userId,
                                                          @PathVariable int orderId) {
        final OrderDto orderDto = orderService.readOneByUserIdAndOrderId(userId, orderId);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<UserDto> readOne(@PathVariable final int id) {
        final UserDto userDto = userService.readOne(id);
        linkUserDto(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    private void linkOrderDto(final OrderDto orderDto) {
        orderDto
                .add(linkTo(methodOn(OrderController.class)
                        .readOne(orderDto.getId()))
                        .withSelfRel());
    }

    private void linkUserDto(final UserDto userDto) {
        userDto.add(linkTo(methodOn(UserController.class)
                .readOne(userDto.getId()))
                .withSelfRel());

        userDto
                .getDtoOrders()
                .forEach(this::linkOrderDto);
    }
}
