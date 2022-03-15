package com.epam.esm.controller;


import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Profile("keycloak")
@RestController
@RequestMapping(value = "/keycloak", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class KeycloakController {

    private final UserService userService;

    @PostMapping(value = "createUser")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createKeycloakUser(@RequestBody final UserDto userDto) {
        return userService.createKeycloakUser(userDto);
    }
}
