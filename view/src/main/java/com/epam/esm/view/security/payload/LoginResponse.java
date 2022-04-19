package com.epam.esm.view.security.payload;

import com.epam.esm.service.dto.RoleDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class LoginResponse {

    private String type = "Bearer";
    private final String token;
    private final String refreshToken;
    private final Integer id;
    private final String username;
    private final String email;
    private final List<RoleDto> roles;
}
