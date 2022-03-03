package com.epam.esm.controller.security;

import com.epam.esm.controller.security.jwt.JwtUserFactory;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final UserDto userDto = userService.readOneByName(username);
        return JwtUserFactory.create(userDto);
    }
}
