package com.epam.esm.dao;

public interface GiftCertificateTagDao {

    void createGiftCertificateTag(int giftCertificateId, int tagId);

    void deleteGiftCertificateTagByCertificateId(int giftCertificateId);

    void deleteGiftCertificateTagByTagId(int tagId);
}
