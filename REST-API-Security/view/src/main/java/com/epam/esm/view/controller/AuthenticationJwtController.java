package com.epam.esm.view.controller;

import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.RefreshTokenDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.view.security.jwt.JwtTokenProvider;
import com.epam.esm.view.security.payload.LoginRequest;
import com.epam.esm.view.security.payload.LoginResponse;
import com.epam.esm.view.security.payload.TokenRefreshRequest;
import com.epam.esm.view.security.payload.TokenRefreshResponse;
import com.epam.esm.view.security.payload.requestvalidator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("jwt")
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthenticationJwtController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RequestValidator requestValidator;
    private final UserService userService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody final LoginRequest request) {
        requestValidator.validateLoginRequest(request);
        final String username = request.getUsername();
        final String password = request.getPassword();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        final UserDto userDto = userService.readOneByName(username);
        final String token = jwtTokenProvider.createToken(userDto);
        final RefreshTokenDto refreshTokenDto = jwtTokenProvider.createRefreshToken(userDto.getId());

        return ResponseEntity.ok(new LoginResponse(token, refreshTokenDto.getToken(), userDto.getId(),
                userDto.getUsername(), userDto.getEmail(), userDto.getDtoRoles()));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody final TokenRefreshRequest request) {
        requestValidator.validateRefreshTokenRequest(request);
        final String requestRefreshToken = request.getRefreshToken();
        final RefreshTokenDto refreshTokenDto = jwtTokenProvider.findByToken(requestRefreshToken);
        final UserDto userDto = refreshTokenDto.getUserDto();
        final String token = jwtTokenProvider.createToken(userDto);
        return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
    }
}
