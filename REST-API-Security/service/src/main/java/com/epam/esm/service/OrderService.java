package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderService {

    OrderDto create(OrderDto orderDto, Integer userId);

    Page<OrderDto> readAll(Pageable pageable, Integer userId);

    Page<OrderDto> readAllByUserId(int userId, Pageable pageable);

    OrderDto readOne(int id);

    OrderDto readOneByUserIdAndOrderId(int userId, int orderId);
}
