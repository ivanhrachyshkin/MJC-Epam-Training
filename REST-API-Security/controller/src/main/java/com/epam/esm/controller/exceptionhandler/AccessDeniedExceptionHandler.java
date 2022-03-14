package com.epam.esm.controller.exceptionhandler;

import com.epam.esm.controller.security.ObjectToJsonMapper;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    private final ObjectToJsonMapper mapper;
    private final ExceptionStatusPostfixProperties properties;

    @Override
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AccessDeniedException e) throws IOException, ServletException {

        final int statusValue = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.
                getWriter()
                .write(mapper.convertObjectToJson(
                        new ApiError(statusValue + properties.getAuth(), e.getMessage())));

    }
}
