package com.epam.esm.service;

import com.epam.esm.model.Order;
import com.epam.esm.service.dto.OrderDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {

    OrderDto create(OrderDto orderDto);

    List<OrderDto> readAll();

    List<OrderDto> readAllByUserId(final int userId);

    OrderDto readOne(int id);

    OrderDto readOneByUserIdAndOrderId(int userId, int orderId);
}
