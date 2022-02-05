package com.epam.esm.dao;

import com.epam.esm.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order create(Order order);

    List<Order> readAll();

    Optional<Order> readOne(int id);

    void deleteById(int id);
}
