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

    private static final String READ_ALL_QUERY
            = "SELECT o FROM Order o JOIN FETCH o.giftCertificate g JOIN FETCH o.user u";
    private static final String READ_ALL_BY_USER_ID_QUERY
            = " SELECT o from Order o JOIN FETCH o.user u JOIN FETCH o.giftCertificate g WHERE u.id = ?1";
    private static final String READ_ONE_BY_USER_ID_AND_GIFT_ID_QUERY
            = "SELECT o FROM Order o JOIN FETCH o.giftCertificate g JOIN FETCH o.user u" +
           " WHERE u.id = ?1 AND g.id = ?2";
    private static final String READ_ONE_BY_USER_ID_AND_ORDER_ID_QUERY
            = " SELECT o from Order o JOIN FETCH o.user u JOIN FETCH o.giftCertificate g WHERE u.id = ?1 AND o.id = ?2";

    @PersistenceContext
    private final EntityManager entityManager;
    private final Clock clock;

    @Override
    public Order create(final Order order) {
        order.setDate(LocalDateTime.now(clock));
        return entityManager.merge(order);
    }

    @SuppressWarnings("JpaQlInspection")
    @Override
    public List<Order> readAll(final Integer page, final Integer size) {
        final TypedQuery<Order> typedQuery
                = entityManager.createQuery(READ_ALL_QUERY, Order.class);
        paginateQuery(typedQuery, page, size);
        return typedQuery.getResultList();
    }

    @Override
    public List<Order> readAllByUserId(final int userId, final Integer page, final Integer size) {
        final TypedQuery<Order> typedQuery
                = entityManager.createQuery(READ_ALL_BY_USER_ID_QUERY, Order.class);
        typedQuery.setParameter(1, userId);
        paginateQuery(typedQuery, page, size);
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
        return query.getResultList().stream().findFirst();
    }


    @Override
    public Optional<Order> readOneByUserIdAndGiftCertificateId(final int userId, final int giftCertificateId) {
        final TypedQuery<Order> query
                = entityManager.createQuery(READ_ONE_BY_USER_ID_AND_GIFT_ID_QUERY, Order.class);
        query.setParameter(1, userId);
        query.setParameter(2, giftCertificateId);
        return query.getResultList().stream().findFirst();
    }

    private void paginateQuery(final TypedQuery<Order> typedQuery, final Integer page, final Integer size) {
        if(page != null && size != null) {
            typedQuery.setFirstResult((page - 1) * size);
            typedQuery.setMaxResults(size);
        }
    }
}
