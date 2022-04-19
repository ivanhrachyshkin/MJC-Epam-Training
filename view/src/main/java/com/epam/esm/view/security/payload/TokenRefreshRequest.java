package com.epam.esm.view.security.payload;

import lombok.Data;

@Data
public class TokenRefreshRequest {

    private String refreshToken;
}
