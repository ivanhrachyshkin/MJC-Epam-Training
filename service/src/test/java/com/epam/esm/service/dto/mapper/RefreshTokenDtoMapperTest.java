package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.RefreshToken;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.RefreshTokenDto;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenDtoMapperTest {

    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private RefreshTokenDtoMapper tokenMapper;

    @Mock
    private RefreshToken refreshToken;
    @Mock
    private RefreshTokenDto dtoRefreshToken;
    @Mock
    private User user;
    @Mock
    private UserDto dtoUser;
    @Mock
    private Role role;
    @Mock
    private RoleDto dtoRole;

    @Test
    void shouldMapTokenDto_For_Token() {
        //Given
        when(refreshToken.getUser()).thenReturn(user);
        when(user.getRoles()).thenReturn(Collections.singletonList(role));
        when(mapper.map(refreshToken, RefreshTokenDto.class)).thenReturn(dtoRefreshToken);
        when(mapper.map(user, UserDto.class)).thenReturn(dtoUser);
        when(mapper.map(role, RoleDto.class)).thenReturn(dtoRole);
        //When
        final RefreshTokenDto actualRefreshTokenDto = tokenMapper.modelToDto(refreshToken);
        //Then
        assertEquals(dtoRefreshToken, actualRefreshTokenDto);
        verify(mapper, times(1)).map(refreshToken, RefreshTokenDto.class);
        verify(mapper, times(1)).map(user, UserDto.class);
        verify(mapper, times(1)).map(role, RoleDto.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldMapToke_For_TokenDto() {
        //Given
        when(dtoRefreshToken.getUserDto()).thenReturn(dtoUser);
        when(mapper.map(dtoRefreshToken, RefreshToken.class)).thenReturn(refreshToken);
        when(mapper.map(dtoUser, User.class)).thenReturn(user);
        //When
        final RefreshToken actualRefreshToken = tokenMapper.dtoToModel(dtoRefreshToken);
        //Then
        assertEquals(refreshToken, actualRefreshToken);
        verify(mapper, times(1)).map(dtoRefreshToken, RefreshToken.class);
        verify(mapper, times(1)).map(dtoUser, User.class);
        verifyNoMoreInteractions(mapper);
    }
}
