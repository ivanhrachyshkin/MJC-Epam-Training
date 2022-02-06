package com.epam.esm.dao;

import com.epam.esm.model.Order;
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

    private static final String READ_QUERY = "SELECT DISTINCT am from Order am inner join am.user ar";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Order create(final Order order) {
        entityManager.persist(order);
        entityManager.flush();
        return order;
    }

    @SuppressWarnings("JpaQlInspection")
    @Override
    public List<Order> readAll(final Integer userId) {

        String query = READ_QUERY;
        TypedQuery<Order> typedQuery;
        if (userId != null) {
            query = query + " WHERE ar.id = ?1";
            typedQuery = entityManager.createQuery(query, Order.class);
            typedQuery.setParameter(1, userId);
        } else {
            typedQuery = entityManager.createQuery(query, Order.class);
        }

        paginateQuery(typedQuery, 1);
        return typedQuery.getResultList();
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
