package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificate create(GiftCertificate giftCertificate);

    List<GiftCertificate> readAll(String tag, String name, String description, Boolean asc);

    GiftCertificate readOne(int id);

    GiftCertificate update(GiftCertificate giftCertificate);

    void deleteById(int id);
}
