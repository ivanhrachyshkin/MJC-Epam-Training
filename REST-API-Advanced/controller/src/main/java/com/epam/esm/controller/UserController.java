package com.epam.esm.controller;


import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final HateoasCreator hateoasCreator;
    private final UserService userService;
    private final OrderService orderService;

    @GetMapping
    public HttpEntity<List<UserDto>> readAll(final Integer page, final Integer size) {
        final List<UserDto> dtoUsers = userService.readAll(page, size);
        dtoUsers.forEach( userDto -> {
            hateoasCreator.linkUserDtoOne(userDto);
            userDto.getDtoOrders().forEach(hateoasCreator::linkOrderDto);
        });

        return new ResponseEntity<>(dtoUsers, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/orders")
    public HttpEntity<List<OrderDto>> readOrdersByUserId(@PathVariable final int userId,
                                                         @RequestParam(required = false) final Integer page,
                                                         @RequestParam(required = false) final Integer size) {
        final List<OrderDto> dtoOrders = orderService.readAllByUserId(userId, page, size);
        dtoOrders.forEach(orderDto -> {
            hateoasCreator.linkOrderDtoOne(orderDto);
            hateoasCreator.linkGiftCertificateDto(orderDto.getGiftCertificateDto());
            hateoasCreator.linkUserDto(orderDto.getUserDto());
        });
        return new ResponseEntity<>(dtoOrders, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/orders/{orderId}")
    public HttpEntity<OrderDto> readOneOrderByUserIdAndOrderId(@PathVariable final int userId,
                                                               @PathVariable final int orderId) {
        final OrderDto orderDto = orderService.readOneByUserIdAndOrderId(userId, orderId);
        hateoasCreator.linkOrderDtoOne(orderDto);
        hateoasCreator.linkUserDto(orderDto.getUserDto());
        hateoasCreator.linkGiftCertificateDto(orderDto.getGiftCertificateDto());
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public HttpEntity<UserDto> readOne(@PathVariable final int id) {
        final UserDto userDto = userService.readOne(id);
        hateoasCreator.linkUserDtoOne(userDto);
        userDto.getDtoOrders().forEach(hateoasCreator::linkOrderDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
