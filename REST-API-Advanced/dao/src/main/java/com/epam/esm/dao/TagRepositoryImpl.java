package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private static final String READ_QUERY = "SELECT t FROM Tag t WHERE t.active = true ";
    private static final String READ_ONE_BY_ID_QUERY = "SELECT t FROM Tag t WHERE t.id = ?1 AND t.active = true";
    private static final String OR_ACTIVE_FALSE = " OR t.active = false";
    private static final String READ_ONE_BY_NAME_QUERY = "SELECT t FROM Tag t WHERE t.name = ?1 ";
    private static final String READ_ONE_MOST_USED =
            "with my_table as" +
                    " (SELECT tags.id, tags.name, tags.active, COUNT(*) as count" +
                    " FROM (select user_id, sum(price)" +
                    " from orders" +
                    " group by user_id" +
                    " having sum(price) >=ALL(select sum(price) from orders group by user_id)" +
                    "     ) as maxcost " +
                    " JOIN orders ON orders.user_id = maxcost.user_id" +
                    " JOIN gift_certificate_tags " +
                    " ON gift_certificate_tags.gift_certificate_id = orders.gift_certificate_id" +
                    " JOIN tags ON tags.id = gift_certificate_tags.tag_id" +
                    " GROUP BY tags.id, tags.name)" +
                    "" +
                    "select id, name, active, count" +
                    " from my_table" +
                    " where count >= ALL (select count from my_table)";

    private static final String SOFT_DELETE = "UPDATE Tag t SET t.active = false WHERE t.id = ?1 AND t.active = true";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Tag create(final Tag tag) {
        tag.setActive(true);
        return entityManager.merge(tag);
    }

    @Override
    public List<Tag> readAll(final Boolean active, final Integer page, final Integer size) {
        final String query = getWithActive(READ_QUERY, active);
        final TypedQuery<Tag> typedQuery = entityManager.createQuery(query, Tag.class);
        paginateQuery(typedQuery, page, size);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<Tag> readOne(final int id, final Boolean active) {
        final String query = getWithActive(READ_ONE_BY_ID_QUERY, active);
        final TypedQuery<Tag> typedQuery = entityManager.createQuery(query, Tag.class);
        typedQuery.setParameter(1, id);
        return typedQuery.getResultList().stream().findFirst();
    }

    @Override
    public Optional<Tag> readOneByName(final String name) {
        final TypedQuery<Tag> typedQuery = entityManager.createQuery(READ_ONE_BY_NAME_QUERY, Tag.class);
        typedQuery.setParameter(1, name);
        return typedQuery.getResultList().stream().findFirst();
    }

    @Override
    public List<Tag> readMostUsed() {
        final Query query = entityManager.createNativeQuery(READ_ONE_MOST_USED, Tag.class);
        return query.getResultList();
    }

    @Override
    public Tag deleteById(final int id) {
        final Tag tag = entityManager.find(Tag.class, id);
        Query query = entityManager.createQuery(SOFT_DELETE);
        query.setParameter(1, id).executeUpdate();
        entityManager.flush();
        return tag;
    }

    private String getWithActive(final String query, final Boolean active) {
        return BooleanUtils.isFalse(active) ? query.concat(OR_ACTIVE_FALSE) : query;
    }

    private void paginateQuery(final TypedQuery<Tag> typedQuery, final Integer page, final Integer size) {
        if(page != null && size != null) {
            typedQuery.setFirstResult((page - 1) * size);
            typedQuery.setMaxResults(size);
        }
    }
}
