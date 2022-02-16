package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    List<GiftCertificateDto> readAll(List<String> tags,
                                     String name,
                                     String description,
                                     String dateSort,
                                     String nameSort,
                                     Integer page,
                                     Integer size);

    GiftCertificateDto readOne(int id);

    GiftCertificateDto update(GiftCertificateDto giftCertificateDto);

    GiftCertificateDto deleteById(int id);
}
