package com.epam.esm.dao;

import com.epam.esm.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private static final String READ_ALL_QUERY = "SELECT DISTINCT am from Order am left join am.user ar";
    @SuppressWarnings("JpaQlInspection")
    private static final String READ_ONE_BY_GIFT_ID_AND_USER_ID_QUERY = "SELECT DISTINCT am from Order am " +
            "inner join am.user au inner join am.giftCertificate ag WHERE au.id = ?1 AND ag.id = ?2";
    private static final String READ_ALL_BY_USER_ID_QUERY
            = "SELECT DISTINCT am from Order am left join am.user ar WHERE ar.id = ?1";
    private static final String READ_ONE_BY_USER_ID_AND_ORDER_ID_QUERY
            = "SELECT DISTINCT am from Order am left join am.user ar WHERE ar.id = ?1 AND am.id = ?2";

    @PersistenceContext
    private final EntityManager entityManager;
    private final Clock clock;

    @Override
    public Order create(final Order order) {
        order.setId(null);
        order.setDate(LocalDateTime.now(clock));
        entityManager.persist(order);
        return order;
    }

    @SuppressWarnings("JpaQlInspection")
    @Override
    public List<Order> readAll() {
        final TypedQuery<Order> typedQuery = entityManager.createQuery(READ_ALL_QUERY, Order.class);
        paginateQuery(typedQuery, 1);
        return typedQuery.getResultList();
    }

    @Override
    public List<Order> readAllByUserId(final int userId) {
        final TypedQuery<Order> typedQuery = entityManager.createQuery(READ_ALL_BY_USER_ID_QUERY, Order.class);
        typedQuery.setParameter(1, userId);
        paginateQuery(typedQuery, 1);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<Order> readOne(final int id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    @Override
    public Optional<Order> readOneByUserIdAndOrderId(final int userId, final int orderId) {
        final TypedQuery<Order> query
                = entityManager.createQuery(READ_ONE_BY_USER_ID_AND_ORDER_ID_QUERY, Order.class);
        query.setParameter(1, userId);
        query.setParameter(2, orderId);
        final List<Order> giftCertificates = query.getResultList();
        return giftCertificates.isEmpty()
                ? Optional.empty()
                : Optional.of(giftCertificates.get(0));
    }



    @Override
    public Optional<Order> readOneByUserIdAndGiftCertificateId(final int userId, final int giftCertificateId) {
        final TypedQuery<Order> query
                = entityManager.createQuery(READ_ONE_BY_GIFT_ID_AND_USER_ID_QUERY, Order.class);
        query.setParameter(1, userId);
        query.setParameter(2, giftCertificateId);
        final List<Order> giftCertificates = query.getResultList();
        return giftCertificates.isEmpty()
                ? Optional.empty()
                : Optional.of(giftCertificates.get(0));
    }

    private void paginateQuery(final TypedQuery<Order> typedQuery, final int pageNumber) { // todo flexible pagination
        typedQuery.setFirstResult(pageNumber - 1);
        typedQuery.setMaxResults(10);
    }
}
