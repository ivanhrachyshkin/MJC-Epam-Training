package com.epam.esm.controller;


import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final HateoasCreator hateoasCreator;
    private final UserService userService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody final UserDto userDto) {
        final UserDto createdUserDto = userService.create(userDto);
        hateoasCreator.linkUserDto(createdUserDto);
        createdUserDto.getDtoOrders().forEach(hateoasCreator::linkOrderDto);
        final HttpHeaders httpHeaders = setLocationHeader(createdUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(createdUserDto);
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
        hateoasCreator.linkGiftCertificateDto(orderDto.getGiftCertificateDto());
        return orderDto;
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

    private HttpHeaders setLocationHeader(final UserDto userDto) {
        final String href = linkTo(methodOn(UserController.class)
                .readOne(userDto.getId()))
                .withSelfRel().getHref();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.LOCATION, href);
        return httpHeaders;
    }
}
