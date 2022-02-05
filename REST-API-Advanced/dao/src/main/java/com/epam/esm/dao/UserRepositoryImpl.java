package com.epam.esm.dao;

import com.epam.esm.model.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private static final String CREATE_QUERY = "INSERT INTO tag (name) VALUES (:name)";
    private static final String READ_QUERY = "SELECT e FROM User e";
    private static final String READ_ONE_BY_NAME_QUERY = "SELECT e FROM Tag e WHERE e.name = ?1";

    private final SessionFactory sessionFactory;

    @Override
    public List<User> readAll() {
        final Session session = sessionFactory.getCurrentSession();
        final TypedQuery<User> query = session.createQuery(READ_QUERY, User.class);
        query.setFirstResult(0);// todo flexible pagination
        query.setMaxResults(10);
        return query.getResultList();
    }

    @Override
    public Optional<User> readOne(final int id) {
        final Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(User.class, id));
    }

    @Override
    public Optional<User> readOneByEmail(final String name) {
        final Session session = sessionFactory.getCurrentSession();
        final TypedQuery<User> query
                = session.createQuery(READ_ONE_BY_NAME_QUERY, User.class);
        final List<User> users = query.setParameter(1, name).getResultList();
        return users.isEmpty()
                ? Optional.empty()
                : Optional.of(users.get(0));
    }
}
