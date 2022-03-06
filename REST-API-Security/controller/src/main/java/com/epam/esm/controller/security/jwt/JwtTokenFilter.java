package com.epam.esm.controller.security.jwt;

import com.epam.esm.controller.exceptionhandler.ApiError;
import com.epam.esm.controller.security.ObjectToJsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectToJsonMapper mapper;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            provideToken(req);
            filterChain.doFilter(req, res);
        } catch (final JwtAuthenticationException e) {
            sendApiErrorAuthentication((HttpServletResponse) res, e);
        }
    }

    private void provideToken(final ServletRequest req) {
        final String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            final Authentication auth = jwtTokenProvider.getAuthentication(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
    }

    private void sendApiErrorAuthentication(final HttpServletResponse response,
                                            final JwtAuthenticationException e) throws IOException {
        final int statusValue = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.
                getWriter()
                .write(mapper.convertObjectToJson(
                        new ApiError(statusValue + e.getPostfix(), e.getMessage())));
    }
}
