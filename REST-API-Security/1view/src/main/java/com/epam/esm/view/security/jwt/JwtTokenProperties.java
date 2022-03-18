package com.epam.esm.view.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@NoArgsConstructor
public class JwtTokenProperties {

    private String secret;
    private String jwtExpirationMs;
    private String jwtRefreshExpirationMs;
}
