package com.epam.esm.service.validator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;

@Component
public class AuthorityValidator {

    public boolean validateAuthorityAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(ADMIN));
    }
}
