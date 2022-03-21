package com.epam.esm.view.security;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.view.exceptionhandler.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectToJsonMapper mapper;
    private final ExceptionStatusPostfixProperties properties;

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException e) throws IOException {
        final int statusValue = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.
                getWriter()
                .write(mapper.convertObjectToJson(
                        new ApiError(statusValue + properties.getAuth(), e.getMessage())));
    }
}
