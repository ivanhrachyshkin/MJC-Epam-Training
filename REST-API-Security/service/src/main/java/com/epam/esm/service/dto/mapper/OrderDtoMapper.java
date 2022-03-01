package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderDtoMapper implements DtoMapper<Order, OrderDto> {

    private final ModelMapper modelMapper;

    @Override
    public OrderDto modelToDto(final Order order) {
        final OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        orderDto.setUserDto(new UserDto(order.getUser().getId()));
        orderDto.setGiftCertificateDto(new GiftCertificateDto(order.getGiftCertificate().getId()));
        return orderDto;
    }

    @Override
    public Order dtoToModel(final OrderDto orderDto) {
        final Order order = modelMapper.map(orderDto, Order.class);
        final User user = modelMapper.map(orderDto.getUserDto(), User.class);
        final GiftCertificate giftCertificate
                = modelMapper.map(orderDto.getGiftCertificateDto(), GiftCertificate.class);
        order.setUser(user);
        order.setGiftCertificate(giftCertificate);
        return order;
    }

    @Override
    public Page<OrderDto> modelsToDto(Page<Order> orders) {
        List<OrderDto> collect = orders
                .stream()
                .map(this::modelToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(collect, orders.getPageable(), orders.getTotalElements());
    }

    @Override
    public List<Order> dtoToModels(List<OrderDto> dtoOrders) {
        return dtoOrders
                .stream()
                .map(this::dtoToModel)
                .collect(Collectors.toList());
    }
}
