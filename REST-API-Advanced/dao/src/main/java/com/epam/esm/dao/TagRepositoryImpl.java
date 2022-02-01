package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        return null;
    }

    @Override
    public Tag readOne(final int id) {
       final Session session = sessionFactory.getCurrentSession();
        return session.get(Tag.class, id);
    }

    @Override
    public Optional<Tag> readOneByName(final String name) {
        return null;
    }

    @Override
    public void deleteById(final int id) {
    }
}
