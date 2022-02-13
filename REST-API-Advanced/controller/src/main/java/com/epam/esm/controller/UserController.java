package com.epam.esm.controller;


import com.epam.esm.service.UserService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<UserDto>> readAll() {
        final List<UserDto> dtoUsers
                = userService.readAll();

        dtoUsers.forEach(this::linkUserDto);
        return new ResponseEntity<>(dtoUsers, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    public ResponseEntity<EntityModel<UserDto>> readOne(@PathVariable final int id) {
        final UserDto userDto = userService.readOne(id);
        final EntityModel<UserDto> userDtoEntityModel = linkUserDtoHal(userDto);
        return new ResponseEntity<>(userDtoEntityModel, HttpStatus.OK);
    }

    private EntityModel<UserDto> linkUserDtoHal(final UserDto userDto) {
        userDto
                .getDtoOrders()
                .forEach(this::linkOrderDto);

        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).readOne(userDto.getId())).withSelfRel());
    }

    private void linkUserDto(final UserDto userDto) {
        userDto.add(linkTo(methodOn(UserController.class)
                .readOne(userDto.getId()))
                .withSelfRel());

        userDto
                .getDtoOrders()
                .forEach(this::linkOrderDto);
    }

    private void linkOrderDto(final OrderDto orderDto) {
        orderDto
                .add(linkTo(methodOn(OrderController.class)
                        .readOne(orderDto.getId()))
                        .withSelfRel());
    }
}
