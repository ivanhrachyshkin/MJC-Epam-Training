package com.epam.esm.view.security.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRefreshResponse {

    private final String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;
}
