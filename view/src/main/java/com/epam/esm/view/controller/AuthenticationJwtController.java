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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;
import static com.epam.esm.service.dto.RoleDto.Roles.USER;

@Profile("jwt")
@Controller
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthenticationJwtController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RequestValidator requestValidator;
    private final UserService userService;

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@ModelAttribute(value = "loginRequest") LoginRequest request, Model model) {
        requestValidator.validateLoginRequest(request);
        final String username = request.getUsername();
        final String password = request.getPassword();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        final UserDto userDto = userService.readOneByName(username);
        final String token = jwtTokenProvider.createToken(userDto);
        final RefreshTokenDto refreshTokenDto = jwtTokenProvider.createRefreshToken(userDto.getId());

        final LoginResponse loginResponse = new LoginResponse(token, refreshTokenDto.getToken(), userDto.getId(),
                userDto.getUsername(), userDto.getEmail(), userDto.getDtoRoles());

        model.addAttribute("loginResponse", loginResponse);

        return "success";
    }

    @Secured({USER, ADMIN})
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody final TokenRefreshRequest request) {
        requestValidator.validateRefreshTokenRequest(request);
        final String requestRefreshToken = request.getRefreshToken();
        final RefreshTokenDto refreshTokenDto = jwtTokenProvider.findRefreshToken(requestRefreshToken);
        final UserDto userDto = refreshTokenDto.getUserDto();
        final String token = jwtTokenProvider.createToken(userDto);
        return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
    }
}
