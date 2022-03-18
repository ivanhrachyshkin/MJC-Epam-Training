package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDto create(OrderDto orderDto);

    Page<OrderDto> readAll(Pageable pageable);

    Page<OrderDto> readAllByUserId(int userId, Pageable pageable);

    OrderDto readOne(int id);

    OrderDto readOneByUserIdAndOrderId(int userId, int orderId);
}
