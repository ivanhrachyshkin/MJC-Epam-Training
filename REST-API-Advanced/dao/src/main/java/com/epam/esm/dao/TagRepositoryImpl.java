package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private static final String READ_QUERY = "SELECT e FROM Tag e WHERE e.active = ?2";
    private static final String READ_ONE_BY_ID_QUERY = "SELECT e FROM Tag e WHERE e.id = ?1 AND e.active = ?2";
    private static final String READ_ONE_BY_NAME_QUERY =
            "SELECT e FROM Tag e WHERE e.name = ?1 ";
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

    private static final String SOFT_DELETE = "UPDATE Tag e SET e.active = false WHERE e.id = ?1 AND e.active = true";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Tag create(final Tag tag) {
        tag.setActive(true);
        return entityManager.merge(tag);
    }

    @Override
    public List<Tag> readAll(final Boolean active, final Integer page, final Integer size) {
        final TypedQuery<Tag> typedQuery = entityManager.createQuery(READ_QUERY, Tag.class);
        setActiveParameter(typedQuery, active);
        paginateQuery(typedQuery, page, size);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<Tag> readOne(final int id, final Boolean active) {
        final TypedQuery<Tag> typedQuery
                = entityManager.createQuery(READ_ONE_BY_ID_QUERY, Tag.class);
        setActiveParameter(typedQuery, active);
        final List<Tag> tags = typedQuery.setParameter(1, id).getResultList();
        return tags.isEmpty()
                ? Optional.empty()
                : Optional.of(tags.get(0));
    }

    @Override
    public Optional<Tag> readOneByName(final String name) {
        final TypedQuery<Tag> typedQuery
                = entityManager.createQuery(READ_ONE_BY_NAME_QUERY, Tag.class);
        final List<Tag> tags = typedQuery.setParameter(1, name).getResultList();
        return tags.isEmpty()
                ? Optional.empty()
                : Optional.of(tags.get(0));
    }

    @Override
    public List<Tag> readMostUsed() {
        final Query query
                = entityManager.createNativeQuery(READ_ONE_MOST_USED, Tag.class);
        return query.getResultList();//todo
    }


    @Override
    public Tag deleteById(final int id) {
        final Tag tag = entityManager.find(Tag.class, id);
        Query query = entityManager.createQuery(SOFT_DELETE);
        query.setParameter(1, id).executeUpdate();
        entityManager.flush();
        return tag;
    }

    private void setActiveParameter(final TypedQuery<Tag> typedQuery, final Boolean active) {
        if (active != null && !active) {
            typedQuery.setParameter(2, false);
        } else {
            typedQuery.setParameter(2, true);
        }
    }

    private void paginateQuery(final TypedQuery<Tag> typedQuery, final Integer page, final Integer size) {
        if (page != null && size != null) {
            typedQuery.setFirstResult((page - 1) * size);
            typedQuery.setMaxResults(size);
        }
    }
}
