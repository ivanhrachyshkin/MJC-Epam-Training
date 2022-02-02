package com.epam.esm.dao;


import com.epam.esm.model.GiftCertificate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String UPDATE_QUERY = "UPDATE gift_certificate SET %s WHERE id = :id";

    private static final String READ_QUERY = "SELECT e FROM GiftCertificate e";
    private static final String READ_ONE_BY_NAME_QUERY = "SELECT e FROM GiftCertificate e WHERE e.name = ?1";

    private final SessionFactory sessionFactory;
    private final Clock clock;

    public GiftCertificateRepositoryImpl(final SessionFactory sessionFactory, final Clock clock) {
        this.sessionFactory = sessionFactory;
        this.clock = clock;
    }


    @Override
    public GiftCertificate create(final GiftCertificate giftCertificate) {
        giftCertificate.setCreateDate(LocalDateTime.now(clock));
        giftCertificate.setLastUpdateDate(LocalDateTime.now(clock));
        final Session session = sessionFactory.getCurrentSession();
        final Serializable id = session.save(giftCertificate);
        session.flush();
        session.clear();
        giftCertificate.setId((Integer) id);
        return giftCertificate;
    }

    @Override
    @Transactional
    public List<GiftCertificate> readAll(final String tag,
                                         final String name,
                                         final String description,
                                         final Boolean dateSortDirection,
                                         final Boolean nameSortDirection) {
        final Session session = sessionFactory.getCurrentSession();

        final Set<String> whereCriteria = new LinkedHashSet<>();
        final Set<String> sortCriteria = new LinkedHashSet<>();
        final Map<Integer, Object> values = new HashMap<>();

//        if (tag != null) {
//            whereCriteria.add("e.tag.name LIKE ?1");
//            values.put(1, tag); // todo
//        }

        if (name != null) {
            whereCriteria.add("e.name LIKE ?2");
            values.put(2, name);
        }

        if (description != null) {
            whereCriteria.add("e.description LIKE ?3");
            values.put(3, description);
        }

        String where = "";
        if (!whereCriteria.isEmpty()) {
            where = " WHERE " + String.join(" AND ", whereCriteria);
        }

        if (dateSortDirection != null) {
            sortCriteria.add("e.createDate " + (dateSortDirection ? "ASC" : "DESC"));
        }

        if (nameSortDirection != null) {
            sortCriteria.add("e.name " + (nameSortDirection ? "ASC" : "DESC"));
        }

        String sort = "";
        if (!sortCriteria.isEmpty()) {
            sort = " ORDER BY " + String.join(", ", sortCriteria);
        }

        final String finalQuery = READ_QUERY + where + sort;
        System.out.println(finalQuery);
        final TypedQuery<GiftCertificate> query = session.createQuery(finalQuery, GiftCertificate.class);
        query.setFirstResult(0);// todo flexible pagination and methods
        query.setMaxResults(10);
        if (!values.isEmpty()) {
            values.forEach((position, value) -> {query.setParameter(position, "%" + value + "%");
            });
        }
        return query.getResultList();
    }

    @Override
    public Optional<GiftCertificate> readOne(final int id) {
        final Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(GiftCertificate.class, id));
    }

    @Override
    public Optional<GiftCertificate> readOneByName(final String name) {
        final Session session = sessionFactory.getCurrentSession();
        final TypedQuery<GiftCertificate> query
                = session.createQuery(READ_ONE_BY_NAME_QUERY, GiftCertificate.class);
        final List<GiftCertificate> giftCertificates = query.setParameter(1, name).getResultList();
        return giftCertificates.isEmpty()
                ? Optional.empty()
                : Optional.of(giftCertificates.get(0));
    }

    @Override
    public void update(final GiftCertificate giftCertificate) {
//        final Map<String, Object> columnToPlaceholder = new HashMap<>();
//        final Map<String, Object> columnToValue = new HashMap<>();
//
//        columnToValue.put("id", giftCertificate.getId());
//
//        if (giftCertificate.getName() != null) {
//            columnToPlaceholder.put("name", ":name");
//            columnToValue.put("name", giftCertificate.getName());
//        }
//
//        if (giftCertificate.getDescription() != null) {
//            columnToPlaceholder.put("description", ":description");
//            columnToValue.put("description", giftCertificate.getDescription());
//        }
//
//        if (giftCertificate.getPrice() != null) {
//            columnToPlaceholder.put("price", ":price");
//            columnToValue.put("price", giftCertificate.getPrice());
//        }
//
//        if (giftCertificate.getDuration() != null) {
//            columnToPlaceholder.put("duration", ":duration");
//            columnToValue.put("duration", giftCertificate.getDuration());
//        }
//
//        columnToPlaceholder.put("last_update_date", ":last_update_date");
//        columnToValue.put("last_update_date", Timestamp.valueOf(LocalDateTime.now(clock)));
//
//        final MapSqlParameterSource namedParameters = new MapSqlParameterSource(columnToValue);
//        final String query = String.format(UPDATE_QUERY, columnToPlaceholder
//                .entrySet()
//                .stream()
//                .map(entry -> entry.getKey() + "=" + entry.getValue())
//                .collect(Collectors.joining(", ")));
//
//      namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public void deleteById(final int id) {
        final Session session = sessionFactory.getCurrentSession();
        final GiftCertificate giftCertificate = session.get(GiftCertificate.class, id);
        session.remove(giftCertificate);
        session.flush();
        session.clear();
    }
}