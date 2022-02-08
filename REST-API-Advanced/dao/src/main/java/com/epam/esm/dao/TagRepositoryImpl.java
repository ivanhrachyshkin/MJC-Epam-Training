package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private static final String READ_QUERY = "SELECT e FROM Tag e";
    private static final String READ_ONE_BY_NAME_QUERY = "SELECT e FROM Tag e WHERE e.name = ?1";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Tag create(final Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public List<Tag> readAll() {
        final TypedQuery<Tag> query = entityManager.createQuery(READ_QUERY, Tag.class);
        paginateQuery(query, 1);
        return query.getResultList();
    }

    @Override
    public Optional<Tag> readOne(final int id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public Optional<Tag> readOneByName(final String name) {
        final TypedQuery<Tag> query
                = entityManager.createQuery(READ_ONE_BY_NAME_QUERY, Tag.class);
        final List<Tag> tags = query.setParameter(1, name).getResultList();
        return tags.isEmpty()
                ? Optional.empty()
                : Optional.of(tags.get(0));
    }

    @Override
    public void deleteById(final int id) {
        final Tag tag = entityManager.find(Tag.class, id);
        entityManager.remove(tag);
    }

    private void paginateQuery(final TypedQuery<Tag> typedQuery, final int pageNumber) {
        typedQuery.setFirstResult(pageNumber - 1);
        typedQuery.setMaxResults(10);
    }
}
