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

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final HateoasCreator hateoasCreator;
    private final UserService userService;
    private final OrderService orderService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<UserDto>> readAll() {
        final List<UserDto> dtoUsers
                = userService.readAll();

        dtoUsers.forEach(hateoasCreator::linkUserDtoWithOrders);
        return new ResponseEntity<>(dtoUsers, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<OrderDto>> readAllByUserId(@PathVariable int userId) {
        final List<OrderDto> dtoOrders
                = orderService.readAllByUserId(userId);
        dtoOrders.forEach(hateoasCreator::linkOrderDto);
        return new ResponseEntity<>(dtoOrders, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/orders/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<OrderDto> readAllByUserIdAndOrderId(@PathVariable int userId,
                                                          @PathVariable int orderId) {
        final OrderDto orderDto
                = orderService.readOneByUserIdAndOrderId(userId, orderId);
        hateoasCreator.linkOrderDto(orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    public ResponseEntity<EntityModel<UserDto>> readOne(@PathVariable final int id) {
        final UserDto userDto = userService.readOne(id);
        final EntityModel<UserDto> userDtoEntityModel = hateoasCreator.linkUserDtoHal(userDto);
        return new ResponseEntity<>(userDtoEntityModel, HttpStatus.OK);
    }
}
