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
import org.springframework.http.HttpStatus;
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
public class AuthenticatedUserProviderTest extends AssertionsProvider {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private AuthenticatedUserProvider userProvider;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private User user;

    private ResourceBundle rb;

    @BeforeEach
    public void setUpRb() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(userProvider, "rb", rb);
    }

    @BeforeEach
    public void setUpContext() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("name");
    }

    @Test
    void shouldReturnToken_On_FindByToken() {
        //Given
        when(userRepository.findByUsername("name")).thenReturn(Optional.of(user));
        //When
        final User actualUser = userProvider.getUserFromAuthentication();
        //Then
        assertEquals(user, actualUser);
        verify(userRepository, only()).findByUsername("name");
    }

   @Test
   void shouldThrowException_On_FindByToken() {
       //Given
       when(userRepository.findByUsername("name")).thenReturn(Optional.empty());
       final ServiceException expectedException =  new ServiceException(
               rb.getString("user.exists.name"),
               HttpStatus.NOT_FOUND, properties.getUser());
       //When
       final ServiceException actualException = assertThrows(ServiceException.class,
               () -> userProvider.getUserFromAuthentication());
       //Then
       verify(userRepository, only()).findByUsername("name");
       assertServiceExceptions(expectedException, actualException);
   }
}
