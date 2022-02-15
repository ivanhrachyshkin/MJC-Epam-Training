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

    private final GiftCertificateRepository giftCertificateRepository;
    private final Clock clock;
    private final Faker generator;
    @PersistenceContext
    EntityManager entityManager;

    public void generate() {

        final Set<String> tagNames = new HashSet<>();
        do {
            final String tagName1 = generator.address().city();
            tagNames.add(tagName1);
        } while (tagNames.size() < 10);

        final List<Tag> tags = tagNames
                .stream()
                .map(s -> {
                    final Tag tag = new Tag();
                    tag.setName(s);
                    return tag;
                }).collect(Collectors.toList());

        final Set<String> giftCertificateNames = new HashSet<>();
        do {
            final String giftCertificateName = generator.company().name();
            giftCertificateNames.add(giftCertificateName);
        } while (giftCertificateNames.size() < 100);

        final List<GiftCertificate> giftCertificates = giftCertificateNames
                .stream()
                .map(s -> {
                    final GiftCertificate newGiftCertificate = new GiftCertificate();
                    newGiftCertificate.setName(s);
                    newGiftCertificate.setDescription(generator.company().industry());
                    newGiftCertificate.setPrice(new Random().nextFloat());
                    newGiftCertificate.setDuration(new Random().nextInt());
                    newGiftCertificate.setCreateDate(LocalDateTime.now(clock));
                    newGiftCertificate.setLastUpdateDate(LocalDateTime.now(clock));
                    return newGiftCertificate;
                }).collect(Collectors.toList());

        for (int i = 0; i < giftCertificates.size(); i++) {
            final GiftCertificate giftCertificate = giftCertificates.get(i);
            giftCertificate.setTags(Collections.singleton(tags.get(i / 10)));
            giftCertificateRepository.create(giftCertificate);
        }

        final Set<String> userEmails = new HashSet<>();
        String userEmail;
        do {
            userEmail = generator.name().username().concat("@mail.com");
            userEmails.add(userEmail);
        } while (userEmails.size() < 10);

        final List<User> users = userEmails
                .stream()
                .map(s -> {
                    final User user = new User();
                    user.setEmail(s);
                    entityManager.persist(user);
                    return user;
                }).collect(Collectors.toList());

        for (int i = 0; i < giftCertificates.size(); i++) {
            Order order = new Order();
            order.setUser(users.get(i / 10));
            order.setDate(LocalDateTime.now(clock));
            order.setPrice(giftCertificates.get(i).getPrice());
            order.setGiftCertificate(giftCertificates.get(i));
            entityManager.persist(order);
        }
    }
}
