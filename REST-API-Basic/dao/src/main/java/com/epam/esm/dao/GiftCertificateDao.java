package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {

    GiftCertificate create(GiftCertificate giftCertificate);

    List<GiftCertificate> readAll(String tag,
                                  String name,
                                  String description,
                                  String dateSort,
                                  String nameSort);

    Optional<GiftCertificate> readOne(int id);

    Optional<GiftCertificate> readOneByName(String name);

    void update(GiftCertificate giftCertificate);

    void deleteById(int id);
}
