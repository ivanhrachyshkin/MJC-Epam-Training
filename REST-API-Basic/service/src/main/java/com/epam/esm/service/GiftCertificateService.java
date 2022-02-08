package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    List<GiftCertificateDto> readAll(String tag,
                                     String name,
                                     String description,
                                     String dateSort,
                                     String nameSort);

    GiftCertificateDto readOne(int id);

    GiftCertificateDto update(GiftCertificateDto giftCertificateDto);

    void deleteById(int id);
}
