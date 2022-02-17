package com.epam.esm.dao;


import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String READ_QUERY = "SELECT DISTINCT am FROM GiftCertificate am JOIN am.tags ar ";
    private static final String READ_ONE_BY_NAME_QUERY = "SELECT e FROM GiftCertificate e WHERE e.name = ?1";
    private static final String WHERE = "WHERE ";
    private static final String TAG_NAME_LIKE = "ar.name LIKE ?";
    private static final String NAME_LIKE = "am.name LIKE ?2";
    private static final String DESCRIPTION_LIKE = "am.description LIKE ?3";
    private static final String CREATE_DATE_QUERY = "am.createDate ";
    private static final String NAME_QUERY = "am.name ";
    private static final String WHERE_DELIMITER = " AND ";
    private static final String ORDER_DELIMITER = ", ";
    private static final String ORDER_BY = " ORDER BY ";

    @PersistenceContext
    private final EntityManager entityManager;
    private final TagRepository tagRepository;
    private final Clock clock;

    @Override
    public GiftCertificate create(final GiftCertificate giftCertificate) {
        giftCertificate.setId(null);
        giftCertificate.setCreateDate(LocalDateTime.now(clock));
        giftCertificate.setLastUpdateDate(LocalDateTime.now(clock));
        final Set<Tag> tags = giftCertificate.getTags();
        setTagId(tags);
        return entityManager.merge(giftCertificate);
    }

    @Override
    public List<GiftCertificate> readAll(final List<String> tags,
                                         final String name,
                                         final String description,
                                         final String dateSort,
                                         final String nameSort,
                                         final Integer page,
                                         final Integer size) {
        final Map<Integer, Object> values = new HashMap<>();

        final String wherePostfix = getWherePostfix(values, tags, name, description);
        final String sortPostfix = getSortPostfix(dateSort, nameSort);

        final String finalQuery = READ_QUERY + wherePostfix + sortPostfix;
        final TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(finalQuery, GiftCertificate.class);
        if (!values.isEmpty()) {
            values.forEach((position, value) -> typedQuery.setParameter(position, "%" + value + "%"));
        }

        paginateQuery(typedQuery, page, size);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<GiftCertificate> readOne(final int id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    public Optional<GiftCertificate> readOneByName(final String name) {
        final TypedQuery<GiftCertificate> query
                = entityManager.createQuery(READ_ONE_BY_NAME_QUERY, GiftCertificate.class);
        final List<GiftCertificate> giftCertificates = query.setParameter(1, name).getResultList();
        return giftCertificates.isEmpty()
                ? Optional.empty()
                : Optional.of(giftCertificates.get(0));
    }

    @Override
    public GiftCertificate update(final GiftCertificate giftCertificate) {
        giftCertificate.setLastUpdateDate(LocalDateTime.now(clock));
        final Set<Tag> tags = giftCertificate.getTags();
        setTagId(tags);
        return entityManager.merge(giftCertificate);
    }

    @Override
    public GiftCertificate deleteById(final int id) {
        final GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        entityManager.remove(giftCertificate);
        return giftCertificate;
    }

    private void setTagId(final Set<Tag> tags) {
        tags.forEach(tag -> {
            final Optional<Tag> optionalTag
                    = tagRepository.readOneByName(tag.getName());
            tag.setId(null);
            tag.setActive(true);
            optionalTag.ifPresent(oldTag -> tag.setId(oldTag.getId()));
        });
    }

    private void paginateQuery(final TypedQuery<GiftCertificate> typedQuery, final Integer page, final Integer size) {
        if (page != null && size != null) {
            typedQuery.setFirstResult((page - 1) * size);
            typedQuery.setMaxResults(size);
        }
    }

    private String getSortPostfix(final String dateSort, final String nameSort) {
        final Set<String> sortCriteria = new HashSet<>();
        if (dateSort != null) {
            sortCriteria.add(CREATE_DATE_QUERY + dateSort.toUpperCase());
        }
        if (nameSort != null) {
            sortCriteria.add(NAME_QUERY + nameSort.toUpperCase());
        }
        String sort = StringUtils.EMPTY;
        if (!sortCriteria.isEmpty()) {
            sort = ORDER_BY + String.join(ORDER_DELIMITER, sortCriteria);
        }
        return sort;
    }

    private String getWherePostfix(final Map<Integer, Object> values,
                                   final List<String> tags,
                                   final String name,
                                   final String description) {
        final Set<String> whereCriteria = new LinkedHashSet<>();
        if (tags != null && !tags.isEmpty()) {
            int n = 1;
            tags.forEach(tag -> {
                whereCriteria.add(TAG_NAME_LIKE + n);
                values.put(n, tag);
            });
        }

        if (name != null) {
            whereCriteria.add(NAME_LIKE);
            values.put(2, name);
        }

        if (description != null) {
            whereCriteria.add(DESCRIPTION_LIKE);
            values.put(3, description);
        }

        String where = StringUtils.EMPTY;
        if (!whereCriteria.isEmpty()) {
            where = WHERE + String.join(WHERE_DELIMITER, whereCriteria);
        }
        return where;
    }
}
