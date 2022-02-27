package com.epam.esm.service.security.jwt;

import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDto;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailService implements UserDetailsService {

    private final UserService userService;

    public JwtUserDetailService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserDto userDto = userService.readOneByName(username);
        return JwtUserFactory.create(userDto);
    }
}
