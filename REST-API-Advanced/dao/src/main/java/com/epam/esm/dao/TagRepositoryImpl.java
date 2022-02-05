package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private static final String CREATE_QUERY = "INSERT INTO tag (name) VALUES (:name)";
    private static final String READ_QUERY = "SELECT e FROM Tag e";
    private static final String READ_ONE_BY_NAME_QUERY = "SELECT e FROM Tag e WHERE e.name = ?1";

    private final SessionFactory sessionFactory;

    @Override
    public Tag create(final Tag tag) {
        final Session session = sessionFactory.getCurrentSession();
        final Serializable id = session.save(tag);
        session.flush();
        session.clear();
        tag.setId((Integer) id);
        return tag;
    }

    @Override
    public List<Tag> readAll() {
        final Session session = sessionFactory.getCurrentSession();
        final TypedQuery<Tag> query = session.createQuery(READ_QUERY, Tag.class);
        paginateQuery(query, 1);
        return query.getResultList();
    }

    @Override
    public Optional<Tag> readOne(final int id) {
        final Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Tag.class, id));
    }

    @Override
    public Optional<Tag> readOneByName(final String name) {
        final Session session = sessionFactory.getCurrentSession();
        final TypedQuery<Tag> query
                = session.createQuery(READ_ONE_BY_NAME_QUERY, Tag.class);
        final List<Tag> tags = query.setParameter(1, name).getResultList();
        return tags.isEmpty()
                ? Optional.empty()
                : Optional.of(tags.get(0));
    }

    @Override
    public void deleteById(final int id) {
        final Session session = sessionFactory.getCurrentSession();
        final Tag tag = session.get(Tag.class, id);
        session.remove(tag);
        session.flush();
        session.clear();
    }

    private void paginateQuery(final TypedQuery<Tag> typedQuery, final int pageNumber) {
        typedQuery.setFirstResult(pageNumber - 1);
        typedQuery.setMaxResults(10);
    }
}
