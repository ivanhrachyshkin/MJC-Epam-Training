package com.epam.esm.service;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.User;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserProvider {

    @Setter
    private ResourceBundle rb;
    private final UserRepository userRepository;
    private final ExceptionStatusPostfixProperties properties;

    public User getUserFromAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String name = authentication.getName();
        return userRepository
                .findByUsername(name).orElseThrow(() ->
                        new ServiceException(
                                rb.getString("user.exists.name"),
                                HttpStatus.NOT_FOUND, properties.getUser()));
    }
}
