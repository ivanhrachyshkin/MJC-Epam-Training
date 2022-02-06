package com.epam.esm.dao;

import com.epam.esm.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order create(Order order);

    List<Order> readAll(Integer userId);

    Optional<Order> readOne(int id);

    Optional<Order> readOneByIds( int userId,  int giftCertificateId);

    void deleteById(int id);
}
