package com.epam.esm.controller;


import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
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

/**
 * The intention of controller - handling of the /gift resource.
 */
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<UserDto>> readAll() {
        final List<UserDto> dtoUsers
                = userService.readAll();

        dtoUsers.forEach(userDto -> {
            linkGiftCertificateDto(userDto);
            userDto
                    .getOrders()
                    .forEach(this::linkOrderDto);
        });
        return new ResponseEntity<>(dtoUsers, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<UserDto> readOne(@PathVariable final int id) {
        final UserDto userDto = userService.readOne(id);
        linkGiftCertificateDto(userDto);
        userDto
                .getOrders()
                .forEach(this::linkOrderDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    private void linkOrderDto(final OrderDto orderDto) {
        orderDto
                .add(linkTo(methodOn(UserController.class)
                        .readAll())
                        .withSelfRel());
    }

    private void linkGiftCertificateDto(final UserDto userDto) {
        userDto.add(linkTo(methodOn(UserController.class)
                .readOne(userDto.getId()))
                .withSelfRel());
    }
}
