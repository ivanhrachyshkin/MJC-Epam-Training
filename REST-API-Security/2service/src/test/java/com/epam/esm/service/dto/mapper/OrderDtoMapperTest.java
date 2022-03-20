package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
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
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDtoMapperTest {

    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private OrderDtoMapper orderDtoMapper;

    @Mock
    private Order order;
    @Mock
    private Order order2;
    @Mock
    private OrderDto dtoOrder;
    @Mock
    private OrderDto dtoOrder2;
    @Mock
    private User user;
    @Mock
    private UserDto dtoUser;
    @Mock
    private GiftCertificate giftCertificate;
    @Mock
    private GiftCertificate giftCertificate2;
    @Mock
    private GiftCertificateDto dtoGiftCertificate;
    @Mock
    private GiftCertificateDto dtoGiftCertificate2;

    @Test
    void shouldMapOrderDto_For_Order() {
        //Given
        when(order.getUser()).thenReturn(user);
        when(order.getGiftCertificates()).thenReturn(new HashSet<>(Arrays.asList(giftCertificate, giftCertificate2)));
        when(mapper.map(order, OrderDto.class)).thenReturn(dtoOrder);
        when(mapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(dtoGiftCertificate);
        when(mapper.map(giftCertificate2, GiftCertificateDto.class)).thenReturn(dtoGiftCertificate2);
        //When
        final OrderDto actualOrderDto = orderDtoMapper.modelToDto(order);
        //Then
        assertEquals(dtoOrder, actualOrderDto);
        verify(mapper, times(1)).map(order, OrderDto.class);
        verify(mapper, times(1)).map(giftCertificate, GiftCertificateDto.class);
        verify(mapper, times(1)).map(giftCertificate2, GiftCertificateDto.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldMapOrder_For_OrderDto() {
        //Given
        when(dtoOrder.getUserDto()).thenReturn(dtoUser);
        when(dtoOrder.getDtoGiftCertificates())
                .thenReturn(new HashSet<>(Arrays.asList(dtoGiftCertificate, dtoGiftCertificate2)));
        when(mapper.map(dtoOrder, Order.class)).thenReturn(order);
        when(mapper.map(dtoUser, User.class)).thenReturn(user);
        when(mapper.map(dtoGiftCertificate, GiftCertificate.class)).thenReturn(giftCertificate);
        when(mapper.map(dtoGiftCertificate2, GiftCertificate.class)).thenReturn(giftCertificate2);
        //When
        final Order actualOrder = orderDtoMapper.dtoToModel(dtoOrder);
        //Then
        assertEquals(order, actualOrder);
        verify(mapper, times(1)).map(dtoOrder, Order.class);
        verify(mapper, times(1)).map(dtoUser, User.class);
        verify(mapper, times(1)).map(dtoGiftCertificate, GiftCertificate.class);
        verify(mapper, times(1)).map(dtoGiftCertificate2, GiftCertificate.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldMapDtoOrders_For_Orders() {
        //Given
        final Page<Order> orders = new PageImpl<>(Arrays.asList(order, order2));
        final Page<OrderDto> expectedDtoOrders = new PageImpl<>(Arrays.asList(dtoOrder, dtoOrder2));
        when(order.getUser()).thenReturn(user);
        when(order.getGiftCertificates()).thenReturn(Collections.singleton(giftCertificate));
        when(order2.getUser()).thenReturn(user);
        when(order2.getGiftCertificates()).thenReturn(Collections.singleton(giftCertificate2));
        when(mapper.map(order, OrderDto.class)).thenReturn(dtoOrder);
        when(mapper.map(order2, OrderDto.class)).thenReturn(dtoOrder2);
        when(mapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(dtoGiftCertificate);
        when(mapper.map(giftCertificate2, GiftCertificateDto.class)).thenReturn(dtoGiftCertificate2);
        //When
        final Page<OrderDto> actualDtoOrders = orderDtoMapper.modelsToDto(orders);
        //Then
        assertEquals(expectedDtoOrders, actualDtoOrders);
        assertEquals(expectedDtoOrders.getTotalElements(), actualDtoOrders.getTotalElements());
        assertEquals(expectedDtoOrders.getTotalPages(), actualDtoOrders.getTotalPages());
        verify(mapper, times(1)).map(order, OrderDto.class);
        verify(mapper, times(1)).map(order2, OrderDto.class);
        verify(mapper, times(1)).map(giftCertificate, GiftCertificateDto.class);
        verify(mapper, times(1)).map(giftCertificate2, GiftCertificateDto.class);
        verifyNoMoreInteractions(mapper);
    }
}
