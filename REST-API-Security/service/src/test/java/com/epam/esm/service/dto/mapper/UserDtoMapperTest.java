package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.Order;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDtoMapperTest {

    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private UserDtoMapper userDtoMapper;

    @Mock
    private User user;
    @Mock
    private UserDto dtoUser;
    @Mock
    private Order order;
    @Mock
    private Role role;
    @Mock
    private RoleDto dtoRole;


    @Test
    void shouldMapUserDto_For_UserWithOrderAndRole() {
        //Given
        when(user.getOrders()).thenReturn(Collections.singleton(order));
        when(user.getRoles()).thenReturn(Collections.singletonList(role));
        when(mapper.map(user, UserDto.class)).thenReturn(dtoUser);
        when(mapper.map(role, RoleDto.class)).thenReturn(dtoRole);
        //When
        final UserDto actualUserDto = userDtoMapper.modelToDto(user);
        //Then
        assertEquals(dtoUser, actualUserDto);
        verify(mapper, times(1)).map(user, UserDto.class);
        verify(mapper, times(1)).map(role, RoleDto.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldMapUserDto_For_UserWithNullOrderAndRole() {
        //Given
        when(user.getOrders()).thenReturn(null);
        when(user.getRoles()).thenReturn(null);
        when(mapper.map(user, UserDto.class)).thenReturn(dtoUser);
        //When
        final UserDto actualUserDto = userDtoMapper.modelToDto(user);
        //Then
        assertEquals(dtoUser, actualUserDto);
        verify(mapper, only()).map(user, UserDto.class);
    }

    @Test
    void shouldMapUser_For_UserDto() {
        //Given
        when(mapper.map(dtoUser, User.class)).thenReturn(user);
        //When
        final User actualUser = userDtoMapper.dtoToModel(dtoUser);
        //Then
        assertEquals(user, actualUser);
        verify(mapper, only()).map(dtoUser, User.class);
    }


    @Test
    void shouldMapDtoUser_For_Users() {
        //Given
        when(user.getRoles()).thenReturn(Collections.singletonList(role));
        final Page<User> users = new PageImpl<>(Collections.singletonList(user));
        final Page<UserDto> dtoUsers = new PageImpl<>(Collections.singletonList(dtoUser));

        when(mapper.map(user, UserDto.class)).thenReturn(dtoUser);
        when(mapper.map(role, RoleDto.class)).thenReturn(dtoRole);
        //When
        final Page<UserDto> actualDtoUsers = userDtoMapper.modelsToDto(users);
        //Then
        assertEquals(dtoUsers, actualDtoUsers);
        assertEquals(dtoUsers.getTotalElements(), actualDtoUsers.getTotalElements());
        assertEquals(dtoUsers.getTotalPages(), actualDtoUsers.getTotalPages());
        verify(mapper, times(1)).map(user, UserDto.class);
        verify(mapper, times(1)).map(role, RoleDto.class);
        verifyNoMoreInteractions(mapper);
    }
}
