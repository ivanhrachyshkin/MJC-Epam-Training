package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao {

    GiftCertificate create(GiftCertificate giftCertificate);

    List<GiftCertificate> readAll(String tag,
                                  String name,
                                  String description,
                                  Boolean asc);

    GiftCertificate readOne(int id);

    GiftCertificate readOneByName(String name);

    void update(GiftCertificate giftCertificate);

    void deleteById(int id);
}
