package com.epam.esm.dao;

import com.epam.esm.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findOrdersByUserId(int userId, Pageable pageable);

    Optional<Order> findOrderByUserIdAndId(int userId, int id);
}
