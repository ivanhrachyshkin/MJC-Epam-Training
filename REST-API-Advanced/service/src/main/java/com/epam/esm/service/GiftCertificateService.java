package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    List<GiftCertificateDto> readAll(List<String> tags,
                                     String name,
                                     String description,
                                     Boolean dateSortDirection,
                                     Boolean nameSortDirection);

    GiftCertificateDto readOne(int id);

    GiftCertificateDto update(GiftCertificateDto giftCertificateDto);

    void deleteById(int id);
}
