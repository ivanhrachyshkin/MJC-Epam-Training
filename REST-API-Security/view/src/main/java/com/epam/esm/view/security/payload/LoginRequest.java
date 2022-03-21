package com.epam.esm.view.security.payload;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
}