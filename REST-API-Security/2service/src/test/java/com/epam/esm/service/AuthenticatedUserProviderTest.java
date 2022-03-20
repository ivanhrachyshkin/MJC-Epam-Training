package com.epam.esm.service;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.User;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class AuthenticatedUserProviderTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private AuthenticatedUserProvider userProvider;

    @Mock
    private User user;

    private ResourceBundle rb;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(userProvider, "rb", rb);
    }

    @Test
    void shouldReturnToken_On_FindByToken() {
        //Given
        final SecurityContext securityContext = mock(SecurityContext.class);
        final Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("");
        when(userRepository.findByUsername("")).thenReturn(Optional.of(user));
        //When
        final User actualUser = userProvider.getUserFromAuthentication();
        //Then
        assertEquals(user, actualUser);
    }

   @Test
   void shouldThrowException_On_FindByToken() {
       //Given
       final SecurityContext securityContext = mock(SecurityContext.class);
       final Authentication authentication = mock(Authentication.class);
       when(securityContext.getAuthentication()).thenReturn(authentication);
       SecurityContextHolder.setContext(securityContext);
       when(authentication.getName()).thenReturn("name");
       when(userRepository.findByUsername("name")).thenReturn(Optional.empty());
       final String message
               = String.format(rb.getString("user.exists.name"));
       //When
       final ServiceException serviceException = assertThrows(ServiceException.class,
               () -> userProvider.getUserFromAuthentication());
       //Then
       assertEquals(message, serviceException.getMessage());
       verify(userRepository, only()).findByUsername("name");
   }
}
