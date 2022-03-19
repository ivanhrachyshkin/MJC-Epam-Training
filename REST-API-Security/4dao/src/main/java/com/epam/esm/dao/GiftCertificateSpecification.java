package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class GiftCertificateSpecification {

    public static Specification<GiftCertificate> giftCertificateFiltered(final List<String> tags,
                                                                         final String name,
                                                                         final String description,
                                                                         final Boolean active) {

        return (root, query, criteriaBuilder) -> {

            Predicate predicate = null;
            if (!CollectionUtils.isEmpty(tags)) {
                final Path<Object> tagNamesPath = root.join("tags").get("name");
                final Predicate[] tagPredicates = tags
                        .stream()
                        .map(s -> criteriaBuilder.equal(tagNamesPath, s))
                        .toArray(Predicate[]::new);

                predicate = criteriaBuilder.and(tagPredicates);
            }

            if (name != null) {
                final Predicate giftCertificateNamePredicate
                        = criteriaBuilder.like(root.get("name"), "%" + name + "%");
                predicate = predicate == null
                        ? giftCertificateNamePredicate
                        : criteriaBuilder.and(predicate, giftCertificateNamePredicate);
            }

            if (description != null) {
                final Predicate giftCertificateDescriptionPredicate
                        = criteriaBuilder.like(root.get("description"), "%" + description + "%");
                predicate = predicate == null
                        ? giftCertificateDescriptionPredicate
                        : criteriaBuilder.and(predicate, giftCertificateDescriptionPredicate);
            }

            if (active) {
                final Predicate giftCertificateActivePredicate
                        = criteriaBuilder.equal(root.get("active"), active);
                predicate = predicate == null
                        ? giftCertificateActivePredicate
                        : criteriaBuilder.and(predicate, giftCertificateActivePredicate);
            }
            return predicate;
        };
    }
}
