package com.epam.esm.controller;

import com.epam.esm.controller.security.AuthenticationRequestDto;
import com.epam.esm.controller.security.jwt.JwtTokenProvider;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> login(@RequestBody final AuthenticationRequestDto requestDto) {
        final String username = requestDto.getUsername();
        final String password = requestDto.getPassword();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        final UserDto userDto = userService.readOneByName(username);
        final String token = jwtTokenProvider.createToken(userDto);

        final Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
