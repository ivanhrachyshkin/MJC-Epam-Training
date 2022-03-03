package com.epam.esm.controller.security;

import com.epam.esm.controller.exceptionhandler.ApiError;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("restAuthenticationEntryPoint")
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private final ExceptionStatusPostfixProperties properties;

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException e) throws IOException {
        final int statusValue = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.
                getWriter()
                .write(convertObjectToJson(
                        new ApiError(statusValue + properties.getAuth(), e.getMessage())));
    }

    private String convertObjectToJson(final Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
