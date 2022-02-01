package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private static final String CREATE_QUERY = "INSERT INTO tag (name) VALUES (:name)";
    private static final String READ_QUERY = "SELECT id, name FROM tag";
    private static final String READ_ONE_QUERY = "SELECT id, name FROM tag WHERE id = :id";
    private static final String READ_ONE_BY_NAME_QUERY = "SELECT id, name FROM tag WHERE name = :name";
    private static final String DELETE_QUERY = "DELETE FROM tag WHERE id = :id";

    private final SessionFactory sessionFactory;

    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Tag create(final Tag tag) {
        return tag;
    }

    @Override
    public List<Tag> readAll() {
        final Session session = sessionFactory.getCurrentSession();
        final TypedQuery<Tag> query = session.createQuery("SELECT e FROM Tag e", Tag.class);
        query.setFirstResult(0);// todo flexible pagination
        query.setMaxResults(10);
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
                = session.createQuery("SELECT e FROM Tag e WHERE e.name = ?1", Tag.class);
        return Optional.ofNullable(query.setParameter(1, name).getSingleResult());
    }

    @Override
    public void deleteById(final int id) {
        final Session session = sessionFactory.getCurrentSession();
        final Tag tag = session.get(Tag.class, id);
        session.remove(tag);
        session.flush();
        session.clear();
    }
}
