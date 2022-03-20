package com.epam.esm.service.validator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;

@Component
public class AuthorityValidator {

    public boolean isAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(ADMIN));
    }

    public boolean isNotAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !authentication.getAuthorities().contains(new SimpleGrantedAuthority(ADMIN));
    }
}
