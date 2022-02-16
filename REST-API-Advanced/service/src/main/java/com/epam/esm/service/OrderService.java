package com.epam.esm.service;

import com.epam.esm.model.Order;
import com.epam.esm.service.dto.OrderDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {

    OrderDto create(OrderDto orderDto);

    List<OrderDto> readAll(Integer page, Integer size);

    List<OrderDto> readAllByUserId(int userId, Integer page, Integer size);

    OrderDto readOne(int id);

    OrderDto readOneByUserIdAndOrderId(int userId, int orderId);
}
