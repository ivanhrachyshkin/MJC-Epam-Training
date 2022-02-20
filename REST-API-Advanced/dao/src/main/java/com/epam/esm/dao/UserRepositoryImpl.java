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

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<User> readAll(final Integer page, final Integer size) {
        final TypedQuery<User> typedQuery = entityManager.createQuery(READ_QUERY, User.class);
        paginateQuery(typedQuery, page, size);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<User> readOne(final int id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    private void paginateQuery(final TypedQuery<User> typedQuery, final Integer page, final Integer size) {
        if(page != null && size != null) {
            typedQuery.setFirstResult((page - 1) * size);
            typedQuery.setMaxResults(size);
        }
    }
}
