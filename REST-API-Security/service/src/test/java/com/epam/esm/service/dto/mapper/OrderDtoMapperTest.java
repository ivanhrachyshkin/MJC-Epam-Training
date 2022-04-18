package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.AssertionsProvider;
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

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDtoMapperTest extends AssertionsProvider<OrderDto> {

    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private OrderDtoMapper orderDtoMapper;

    @Mock
    private Order order;
    @Mock
    private OrderDto dtoOrder;
    @Mock
    private User user;
    @Mock
    private UserDto dtoUser;
    @Mock
    private GiftCertificate giftCertificate;
    @Mock
    private GiftCertificateDto dtoGiftCertificate;

    @Test
    void shouldMapOrderDto_For_Order() {
        //Given
        when(order.getUser()).thenReturn(user);
        when(order.getGiftCertificates()).thenReturn(Collections.singleton(giftCertificate));
        when(mapper.map(order, OrderDto.class)).thenReturn(dtoOrder);
        when(mapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(dtoGiftCertificate);
        //When
        final OrderDto actualOrderDto = orderDtoMapper.modelToDto(order);
        //Then
        assertEquals(dtoOrder, actualOrderDto);
        verify(mapper, times(1)).map(order, OrderDto.class);
        verify(mapper, times(1)).map(giftCertificate, GiftCertificateDto.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldMapOrder_For_OrderDto() {
        //Given
        when(dtoOrder.getUserDto()).thenReturn(dtoUser);
        when(dtoOrder.getDtoGiftCertificates())
                .thenReturn(Collections.singleton(dtoGiftCertificate));
        when(mapper.map(dtoOrder, Order.class)).thenReturn(order);
        when(mapper.map(dtoUser, User.class)).thenReturn(user);
        when(mapper.map(dtoGiftCertificate, GiftCertificate.class)).thenReturn(giftCertificate);
        //When
        final Order actualOrder = orderDtoMapper.dtoToModel(dtoOrder);
        //Then
        assertEquals(order, actualOrder);
        verify(mapper, times(1)).map(dtoOrder, Order.class);
        verify(mapper, times(1)).map(dtoUser, User.class);
        verify(mapper, times(1)).map(dtoGiftCertificate, GiftCertificate.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldMapDtoOrders_For_Orders() {
        //Given
        final Page<Order> orders = new PageImpl<>(Collections.singletonList(order));
        final Page<OrderDto> expectedDtoOrders = new PageImpl<>(Collections.singletonList(dtoOrder));
        when(order.getUser()).thenReturn(user);
        when(order.getGiftCertificates()).thenReturn(Collections.singleton(giftCertificate));
        when(mapper.map(order, OrderDto.class)).thenReturn(dtoOrder);
        when(mapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(dtoGiftCertificate);
        //When
        final Page<OrderDto> actualDtoOrders = orderDtoMapper.modelsToDto(orders);
        //Then
        assertPages(expectedDtoOrders, actualDtoOrders);
        verify(mapper, times(1)).map(order, OrderDto.class);
        verify(mapper, times(1)).map(giftCertificate, GiftCertificateDto.class);
        verifyNoMoreInteractions(mapper);
    }
}
