package com.epam.esm.service;

import com.epam.esm.dao.OrderRepository;
import com.epam.esm.model.Order;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DtoMapper<Order, OrderDto> mapper;


    @Override
    @Transactional
    public List<OrderDto> readAll(final Integer userId) {
        final List<Order> orders = orderRepository.readAll(userId);
        return mapper.modelsToDto(orders);
    }

    @Override
    @Transactional
    public OrderDto readOne(final int id) {
        final Order order = checkExist(id);
        return mapper.modelToDto(order);
    }

    private Order checkExist(final int id) {
        return orderRepository
                .readOne(id)
                .orElseThrow(() -> new ServiceException("order.notFound.id", id));
    }
}
