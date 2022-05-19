package com.epam.esm.service;

import com.epam.esm.model.Category;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.dto.CategoryDto;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    Page<GiftCertificateDto> readAll(List<String> tags,
                                     GiftCertificateRequestParamsContainer container,
                                     Pageable pageable);

    Page<GiftCertificateDto> readAllByCategory(CategoryDto categoryDto, Pageable pageable);

    GiftCertificateDto readOne(int id);

    GiftCertificateDto update(GiftCertificateDto giftCertificateDto);

    GiftCertificateDto deleteById(int id);
}
