package com.epam.esm.controller.security.payload;

import lombok.Data;

@Data
public class TokenRefreshRequest {

    private String refreshToken;
}
