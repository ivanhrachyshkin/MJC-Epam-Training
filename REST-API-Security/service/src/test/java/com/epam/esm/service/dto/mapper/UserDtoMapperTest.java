package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDtoMapperTest {

    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private UserDtoMapper userDtoMapper;

    private User user;
    private UserDto userDto;
    private Order order;
    private OrderDto orderDto;

    @BeforeEach
    public void setUp() {
        order = new Order();
        orderDto = new OrderDto();
        order.setId(1);
        orderDto.setId(1);

        user = new User();
        user.setOrders(Collections.singleton(order));
        userDto = new UserDto();
        userDto.setDtoOrders(Collections.singleton(orderDto));
    }

    @Test
    void shouldMapGiftCertificateDto_For_GiftCertificate() {
        //Given
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        //When
        final UserDto actualUserDto = userDtoMapper.modelToDto(user);
        //Then
        assertEquals(userDto, actualUserDto);
        verify(mapper, only()).map(user, UserDto.class);
    }

    @Test
    void shouldMapGiftCertificate_For_GiftCertificateDto() {
        //Given
        when(mapper.map(userDto, User.class)).thenReturn(user);
        when(mapper.map(orderDto, Order.class)).thenReturn(order);
        //When
        final User actualUser = userDtoMapper.dtoToModel(userDto);
        //Then
        assertEquals(user, actualUser);
        verify(mapper, times(1)).map(userDto, User.class);
        verify(mapper, times(1)).map(orderDto, Order.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldMapDtoGiftCertificates_For_GiftCertificates() {
        //Given
        final List<User> users = Collections.singletonList(user);
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        //When
        final List<UserDto> dtoActualUsers = userDtoMapper.modelsToDto(users);
        //Then
        assertEquals(Collections.singletonList(userDto), dtoActualUsers);
        verify(mapper, only()).map(user, UserDto.class);
    }

    @Test
    void shouldMapGiftCertificates_For_GiftCertificateDtos() {
        //Given
        final List<UserDto> dtoUsers = Collections.singletonList(userDto);
        when(mapper.map(userDto, User.class)).thenReturn(user);
        //When
        final List<User> actualUsers = userDtoMapper.dtoToModels(dtoUsers);
        //Then
        assertEquals(Collections.singletonList(user), actualUsers);
        verify(mapper, times(1)).map(userDto, User.class);
        verify(mapper, times(1)).map(orderDto, Order.class);
        verifyNoMoreInteractions(mapper);
    }
}