package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.GiftCertificateDto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class OrderDtoMapperTest {

    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private OrderDtoMapper orderDtoMapper;

    private User user;
    private UserDto userDto;
    private GiftCertificate giftCertificate;
    private GiftCertificateDto giftCertificateDto;
    private Order order;
    private OrderDto orderDto;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        userDto = new UserDto();
        giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        giftCertificateDto = new GiftCertificateDto();

        order = new Order();
        order.setUser(user);
        order.setGiftCertificate(giftCertificate);
        orderDto = new OrderDto();
        orderDto.setUserDto(userDto);
        orderDto.setGiftCertificateDto(giftCertificateDto);
    }

    @Test
    void shouldMapOrderDto_For_Order() {
        //Given
        when(mapper.map(order, OrderDto.class)).thenReturn(orderDto);
        //When
        final OrderDto actualOrderDto = orderDtoMapper.modelToDto(order);
        //Then
        assertEquals(orderDto, actualOrderDto);

        verify(mapper, only()).map(order, OrderDto.class);
    }

    @Test
    void shouldMapUser_For_UserDto() {
        //Given
        when(mapper.map(orderDto, Order.class)).thenReturn(order);
        when(mapper.map(userDto, User.class)).thenReturn(user);
        when(mapper.map(giftCertificateDto, GiftCertificate.class)).thenReturn(giftCertificate);
        //When
        final Order actualOrder = orderDtoMapper.dtoToModel(orderDto);
        //Then
        assertEquals(order, actualOrder);

        verify(mapper, times(1)).map(orderDto, Order.class);
        verify(mapper, times(1)).map(userDto, User.class);
        verify(mapper, times(1)).map(giftCertificateDto, GiftCertificate.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldMapOrderDtos_For_Orders() {
        //Given
        final List<Order> orders = Collections.singletonList(order);
        when(mapper.map(order, OrderDto.class)).thenReturn(orderDto);
        //When
        final List<OrderDto> actualOrderDtos = orderDtoMapper.modelsToDto(orders);
        //Then
        assertEquals(Collections.singletonList(orderDto), actualOrderDtos);

        verify(mapper, only()).map(order, OrderDto.class);
    }

    @Test
    void shouldMapGiftCertificates_For_GiftCertificateDtos() {
        //Given
        final List<OrderDto> orderDtos = Collections.singletonList(orderDto);
        when(mapper.map(orderDto, Order.class)).thenReturn(order);
        when(mapper.map(userDto, User.class)).thenReturn(user);
        when(mapper.map(giftCertificateDto, GiftCertificate.class)).thenReturn(giftCertificate);
        //When
        final List<Order> actualOrders = orderDtoMapper.dtoToModels(orderDtos);
        //Then
        assertEquals(Collections.singletonList(order), actualOrders);

        verify(mapper, times(1)).map(orderDto, Order.class);
        verify(mapper, times(1)).map(userDto, User.class);
        verify(mapper, times(1)).map(giftCertificateDto, GiftCertificate.class);
        verifyNoMoreInteractions(mapper);
    }
}