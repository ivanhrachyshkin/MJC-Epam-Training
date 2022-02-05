package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public Order dtoToModel(final OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }

    @Override
    public List<OrderDto> modelsToDto(List<Order> orders) {
        return orders
                .stream()
                .map(this::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> dtoToModels(List<OrderDto> dtoOrders) {
        return dtoOrders
                .stream()
                .map(this::dtoToModel)
                .collect(Collectors.toList());
    }

    private void emptyTagsIfNull(final User user) {
        if (user.getOrders() == null) {
            user.setOrders(Collections.emptySet());
        }
    }
}
