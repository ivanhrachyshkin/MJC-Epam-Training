package com.epam.esm.controller.security.jwt;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtTokenProperties jwtTokenProperties;
    private final ExceptionStatusPostfixProperties exceptionStatusPostfixProperties;
    private final UserDetailsService userDetailsService;

    private String secret;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(jwtTokenProperties.getSecret().getBytes());
    }

    public String createToken(final UserDto userDto) {
        final Claims claims = Jwts.claims().setSubject(userDto.getUsername());
        claims.put("roles", getRoleNames(userDto.getDtoRoles()));

        final Date date = Date.from(
                LocalDateTime
                        .now()
                        .plusMinutes(Long.parseLong(jwtTokenProperties.getExpired()))
                        .atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        final String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) throws JwtAuthenticationException {
        try {
            final Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("expired or invalid token",
                    HttpStatus.UNAUTHORIZED, exceptionStatusPostfixProperties.getAuth());
        }
    }

    private List<String> getRoleNames(List<RoleDto> dtoRoles) {
        final List<String> result = new ArrayList<>();
        dtoRoles.forEach(role -> result.add(String.valueOf(role.getRoleName())));
        return result;
    }
}
