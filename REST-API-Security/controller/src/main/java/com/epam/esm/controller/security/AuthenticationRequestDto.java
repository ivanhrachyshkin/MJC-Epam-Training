package com.epam.esm.controller.security;
import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String username;
    private String password;
}