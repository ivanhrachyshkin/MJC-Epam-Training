package com.epam.esm.controller;


import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Profile("keycloak")
@RestController
@RequestMapping(value = "/keycloak", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class KeycloakController {

    private final HateoasCreator hateoasCreator;
    private final UserService userService;
    private final OrderService orderService;

    @PostMapping(value = "createUser")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createKeycloakUser(@RequestBody final UserDto userDto) {
        final UserDto createdUser = userService.createKeycloakUser(userDto);
        setLocationHeader(userDto);
        return createdUser;
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
