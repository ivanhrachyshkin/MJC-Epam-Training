package com.epam.esm.dao;

import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private static final String READ_QUERY = "SELECT e FROM Order e";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Order create(final Order order) {
        entityManager.persist(order);
        entityManager.flush();
        return order;
    }

    @Override
    public List<Order> readAll() {
        final TypedQuery<Order> query = entityManager.createQuery(READ_QUERY, Order.class);
        paginateQuery(query, 1);
        return query.getResultList();
    }

    @Override
    public Optional<Order> readOne(final int id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    @Override
    public void deleteById(final int id) {
        final Order order = entityManager.find(Order.class, id);
        entityManager.remove(order);
        entityManager.flush();
    }

    private void paginateQuery(final TypedQuery<Order> typedQuery, final int pageNumber) {
        typedQuery.setFirstResult(pageNumber - 1);
        typedQuery.setMaxResults(10);
    }
}
