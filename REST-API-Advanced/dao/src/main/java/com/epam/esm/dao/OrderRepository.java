package com.epam.esm.dao;

import com.epam.esm.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order create(Order order);

    List<Order> readAll();

    List<Order> readAllByUserId(final int userId);

    Optional<Order> readOne(int id);

    Optional<Order> readOneByUserIdAndOrderId(int userId, int orderId);

    Optional<Order> readOneByUserIdAndGiftCertificateId(int userId, int giftCertificateId);

}
