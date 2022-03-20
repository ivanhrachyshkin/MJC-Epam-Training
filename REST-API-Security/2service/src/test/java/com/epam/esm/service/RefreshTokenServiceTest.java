package com.epam.esm.service;

import com.epam.esm.dao.RefreshTokenRepository;
import com.epam.esm.dao.TagRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.RefreshToken;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.RefreshTokenDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private DtoMapper<RefreshToken, RefreshTokenDto> mapper;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Mock
    private RefreshToken refreshToken;
    @Mock
    private RefreshTokenDto dtoRefreshToken;

    private ResourceBundle rb;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(refreshTokenService, "rb", rb);
    }

    @Test
    void shouldReturnToken_On_FindByToken() {
        //Given
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        when(refreshToken.getExpiryDate()).thenReturn(LocalDateTime.of(2030, 1,1,1,1));
        when(mapper.modelToDto(refreshToken)).thenReturn(dtoRefreshToken);
        //When
        final RefreshTokenDto actualDtoRefreshToken = refreshTokenService.findByToken(anyString());
        //Then
        assertEquals(dtoRefreshToken, actualDtoRefreshToken);
        verify(refreshTokenRepository, only()).findByToken(anyString());
        verify(mapper, only()).modelToDto(refreshToken);
    }

    @Test
    void shouldThrowException_On_FindByToken() {
        //Given
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());
        final String message = rb.getString("token.not.found");
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> refreshTokenService.findByToken(anyString()));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(refreshTokenRepository, only()).findByToken(anyString());
    }

    @Test
    void shouldThrowException_On_FindByTokenExpired() {
        //Given
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        when(refreshToken.getExpiryDate()).thenReturn(LocalDateTime.of(2000, 1,1,1,1));
        final String message = rb.getString("refreshToken.expired.new");
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> refreshTokenService.findByToken(anyString()));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(refreshTokenRepository, times(1)).findByToken(anyString());
        verify(refreshTokenRepository, times(1)).delete(refreshToken);
    }

    @Test
    void shouldThrowException_On_Create() {
        //Given
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        final String message = String.format(rb.getString("user.notFound.id"), 1);
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> refreshTokenService.createRefreshToken(1, 10L));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(userRepository, only()).findById(1);
    }
}
