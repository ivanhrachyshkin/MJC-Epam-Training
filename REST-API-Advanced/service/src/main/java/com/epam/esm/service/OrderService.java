package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;

import java.util.List;

public interface OrderService {

  //  OrderDto create(OrderDto orderDto);

    List<OrderDto> readAll();

    OrderDto readOne(int id);

   // void deleteById(int id);
}
