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

import java.util.Arrays;
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
    private UserDto userDto;
    @Mock
    private User user2;
    @Mock
    private UserDto userDto2;
    @Mock
    private Order order;
    @Mock
    private Role role;
    @Mock
    private RoleDto roleDto;


    @Test
    void shouldMapUserDto_For_UserWithOrderAndRole() {
        //Given
        when(user.getOrders()).thenReturn(Collections.singleton(order));
        when(user.getRoles()).thenReturn(Collections.singletonList(role));
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        when(mapper.map(role, RoleDto.class)).thenReturn(roleDto);
        //When
        final UserDto actualUserDto = userDtoMapper.modelToDto(user);
        //Then
        assertEquals(userDto, actualUserDto);
        verify(mapper, times(1)).map(user, UserDto.class);
        verify(mapper, times(1)).map(role, RoleDto.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldMapUserDto_For_UserWithNullOrderAndRole() {
        //Given
        when(user.getOrders()).thenReturn(null);
        when(user.getRoles()).thenReturn(null);
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        //When
        final UserDto actualUserDto = userDtoMapper.modelToDto(user);
        //Then
        assertEquals(userDto, actualUserDto);
        verify(mapper, only()).map(user, UserDto.class);
    }

    @Test
    void shouldMapUser_For_UserDto() {
        //Given
        when(mapper.map(userDto, User.class)).thenReturn(user);
        //When
        final User actualUser = userDtoMapper.dtoToModel(userDto);
        //Then
        assertEquals(user, actualUser);
        verify(mapper, only()).map(userDto, User.class);
    }


    @Test
    void shouldMapDtoUser_For_Users() {
        //Given
        when(user.getRoles()).thenReturn(Collections.singletonList(role));
        when(user2.getRoles()).thenReturn(Collections.singletonList(role));
        final Page<User> users = new PageImpl<>(Arrays.asList(user, user2));
        final Page<UserDto> dtoUsers = new PageImpl<>(Arrays.asList(userDto, userDto2));

        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        when(mapper.map(user2, UserDto.class)).thenReturn(userDto2);
        when(mapper.map(role, RoleDto.class)).thenReturn(roleDto);
        //When
        final Page<UserDto> actualDtoUsers = userDtoMapper.modelsToDto(users);
        //Then
        assertEquals(dtoUsers, actualDtoUsers);
        assertEquals(dtoUsers.getTotalElements(), actualDtoUsers.getTotalElements());
        assertEquals(dtoUsers.getTotalPages(), actualDtoUsers.getTotalPages());
        verify(mapper, times(1)).map(user, UserDto.class);
        verify(mapper, times(1)).map(user2, UserDto.class);
        verify(mapper, times(2)).map(role, RoleDto.class);
        verifyNoMoreInteractions(mapper);
    }
}
