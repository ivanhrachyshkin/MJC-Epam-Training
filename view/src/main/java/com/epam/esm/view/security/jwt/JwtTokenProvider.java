package com.epam.esm.view.security.jwt;

import com.epam.esm.service.RefreshTokenServiceImpl;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.RefreshTokenDto;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
    private final RefreshTokenServiceImpl refreshTokenService;

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
                        .plusSeconds(Long.parseLong(jwtTokenProperties.getJwtExpirationMs()))
                        .atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(date)
                .setId(String.valueOf(userDto.getId()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String resolveToken(HttpServletRequest req) {
        final String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(final String token) throws JwtAuthenticationException {
        try {
            final Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("token invalid or expired",
                    HttpStatus.UNAUTHORIZED, exceptionStatusPostfixProperties.getAuth());
        }
    }

    public RefreshTokenDto createRefreshToken(final int userId) {
        return refreshTokenService.createRefreshToken(userId,
                Long.parseLong(jwtTokenProperties.getJwtRefreshExpirationMs()));
    }

    public RefreshTokenDto findRefreshToken(final String token) {
        return refreshTokenService.findByToken(token);
    }

    public Authentication getAuthentication(String token) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, StringUtils.EMPTY, userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    private List<String> getRoleNames(List<RoleDto> dtoRoles) {
        final List<String> result = new ArrayList<>();
        dtoRoles.forEach(role -> result.add(String.valueOf(role.getRoleName())));
        return result;
    }
}
