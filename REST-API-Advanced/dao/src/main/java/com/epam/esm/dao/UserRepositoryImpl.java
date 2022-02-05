package com.epam.esm.dao;

import com.epam.esm.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private static final String READ_QUERY = "SELECT e FROM User e";
    private static final String READ_ONE_BY_NAME_QUERY = "SELECT e FROM User e WHERE e.email = ?1";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<User> readAll() {
        final TypedQuery<User> query = entityManager.createQuery(READ_QUERY, User.class);
        paginateQuery(query, 1);
        return query.getResultList();
    }

    @Override
    public Optional<User> readOne(final int id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> readOneByEmail(final String name) {
        final TypedQuery<User> query
                = entityManager.createQuery(READ_ONE_BY_NAME_QUERY, User.class);
        final List<User> users = query.setParameter(1, name).getResultList();
        return users.isEmpty()
                ? Optional.empty()
                : Optional.of(users.get(0));
    }

    private void paginateQuery(final TypedQuery<User> typedQuery, final int pageNumber) {
        typedQuery.setFirstResult(pageNumber - 1);
        typedQuery.setMaxResults(10);
    }
}
