package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GeneratorRepositoryImpl implements GeneratorRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final GiftCertificateRepository giftCertificateRepository;
    private final Faker generator;
    private final Clock clock;
    private final Random random;

    public void generate() {

        final Set<String> tagNames = generateTagNames();
        final List<Tag> tags = createTags(tagNames);

        final Set<String> giftCertificateNames = generateGiftCertificatesNames();
        final List<GiftCertificate> giftCertificates = createGiftCertificates(giftCertificateNames);

        for (int i = 0; i < giftCertificates.size(); i++) {
            final GiftCertificate giftCertificate = giftCertificates.get(i);
            giftCertificate.setTags(Collections.singleton(tags.get(i / 10)));
            giftCertificateRepository.create(giftCertificate);
        }

        final Set<String> userEmails = generateUsersEmails();
        final List<User> users = createUsers(userEmails);

        for (int i = 0; i < giftCertificates.size(); i++) {
            Order order = new Order();
            order.setUser(users.get(i / 10));
            order.setDate(LocalDateTime.now(clock));
            order.setPrice(giftCertificates.get(i).getPrice());
            order.setGiftCertificate(giftCertificates.get(i));
            entityManager.persist(order);
        }
    }

    private Set<String> generateTagNames() {
        final Set<String> tagNames = new HashSet<>();
        do {
            final String tagName1 = generator.address().city();
            tagNames.add(tagName1);
        } while (tagNames.size() < 10);
        return tagNames;
    }

    private List<Tag> createTags(final Set<String> tagNames) {
        return tagNames
                .stream()
                .map(name -> {
                    final Tag tag = new Tag();
                    tag.setName(name);
                    return tag;
                }).collect(Collectors.toList());
    }

    private Set<String> generateGiftCertificatesNames() {
        final Set<String> giftCertificateNames = new HashSet<>();
        do {
            final String giftCertificateName = generator.company().name();
            giftCertificateNames.add(giftCertificateName);
        } while (giftCertificateNames.size() < 100);
        return giftCertificateNames;
    }

    private List<GiftCertificate> createGiftCertificates(final Set<String> giftCertificateNames) {
        return giftCertificateNames
                .stream()
                .map(s -> {
                    final GiftCertificate newGiftCertificate = new GiftCertificate();
                    newGiftCertificate.setName(s);
                    newGiftCertificate.setDescription(generator.company().industry());
                    newGiftCertificate.setPrice(random.nextFloat());
                    newGiftCertificate.setDuration(random.nextInt());
                    newGiftCertificate.setCreateDate(LocalDateTime.now(clock));
                    newGiftCertificate.setLastUpdateDate(LocalDateTime.now(clock));
                    return newGiftCertificate;
                }).collect(Collectors.toList());
    }

    private Set<String> generateUsersEmails() {
        final Set<String> usersEmails = new HashSet<>();
        do {
            final String userEmail = generator.name().username().concat("@mail.com");
            usersEmails.add(userEmail);
        } while (usersEmails.size() < 10);
        return usersEmails;
    }

    private List<User> createUsers(final Set<String> usersEmails) {
        return usersEmails
                .stream()
                .map(s -> {
                    final User user = new User();
                    user.setEmail(s);
                    entityManager.persist(user);
                    return user;
                }).collect(Collectors.toList());
    }

}
