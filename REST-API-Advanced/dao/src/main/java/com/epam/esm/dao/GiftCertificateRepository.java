package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {

    GiftCertificate create(GiftCertificate giftCertificate);

    List<GiftCertificate> readAll(List<String> tags,
                                  String name,
                                  String description,
                                  Boolean dateSortDirection,
                                  Boolean nameSortDirection);

    Optional<GiftCertificate> readOne(int id);

    Optional<GiftCertificate> readOneByName(String name);

    GiftCertificate update(GiftCertificate giftCertificate);

    GiftCertificate deleteById(int id);
}
