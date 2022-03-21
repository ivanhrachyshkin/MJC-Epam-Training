package com.epam.esm.service.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashSet;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;
import static com.epam.esm.service.dto.RoleDto.Roles.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorityValidatorTest {

    @InjectMocks
    private AuthorityValidator authorityValidator;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    private Collection authorities;

    @BeforeEach
    public void setUp() {
        authorities = new HashSet();
    }

    @Test
    void shouldReturnTrue_On_IsAdmin() {
        //Given
        authorities.add(new SimpleGrantedAuthority(ADMIN));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(authorities);
        //When
        final Boolean actual = authorityValidator.isAdmin();
        //Then
        assertEquals(true, actual);
    }

    @Test
    void shouldReturnFalse_On_IsAdmin() {
        //Given
        authorities.add(new SimpleGrantedAuthority(USER));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(authorities);
        //When
        final Boolean actual = authorityValidator.isAdmin();
        //Then
        assertEquals(false, actual);
    }

    @Test
    void shouldReturnTrue_On_IsNotAdmin() {
        //Given
        authorities.add(new SimpleGrantedAuthority(USER));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(authorities);
        //When
        final Boolean actual = authorityValidator.isNotAdmin();
        //Then
        assertEquals(true, actual);
    }

    @Test
    void shouldReturnFalse_On_IsNotAdmin() {
        //Given
        authorities.add(new SimpleGrantedAuthority(ADMIN));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(authorities);
        //When
        final Boolean actual = authorityValidator.isNotAdmin();
        //Then
        assertEquals(false, actual);
    }
}
