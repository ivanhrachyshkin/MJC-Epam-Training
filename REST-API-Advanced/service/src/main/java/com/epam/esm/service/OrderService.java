package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;

import java.util.List;

public interface OrderService {

    //  OrderDto create(OrderDto orderDto);

    List<OrderDto> readAll(Integer userId);

    OrderDto readOne(int id);

    // void deleteById(int id);
}
