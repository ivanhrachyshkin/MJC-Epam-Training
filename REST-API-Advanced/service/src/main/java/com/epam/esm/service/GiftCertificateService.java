package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    List<GiftCertificateDto> readAll(List<String> tags,
                                     GiftCertificateRequestParamsContainer container,
                                     Integer page,
                                     Integer size);

    GiftCertificateDto readOne(int id);

    GiftCertificateDto update(GiftCertificateDto giftCertificateDto);

    GiftCertificateDto deleteById(int id);
}
