package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.GiftCertificateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final DtoMapper<GiftCertificate, GiftCertificateDto> mapper;
    private final GiftCertificateValidator giftCertificateValidator;

    @Override
    @Transactional
    public GiftCertificateDto create(final GiftCertificateDto giftCertificateDto) {
        giftCertificateValidator.createValidate(giftCertificateDto);
        final GiftCertificate giftCertificate = mapper.dtoToModel(giftCertificateDto);
        checkExistByName(giftCertificate.getName());
        final GiftCertificate newGiftCertificate = giftCertificateRepository.create(giftCertificate);
        return mapper.modelToDto(newGiftCertificate);
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> readAll(final String tag,
                                            final String name,
                                            final String description,
                                            final Boolean dateSortDirection,
                                            final Boolean nameSortDirection) {
        giftCertificateValidator.readAllValidate(tag, name, description);
        List<GiftCertificate> giftCertificates
                = giftCertificateRepository.readAll(tag, name, description, dateSortDirection, nameSortDirection);
        return mapper.modelsToDto(giftCertificates);
    }

    @Override
    @Transactional
    public GiftCertificateDto readOne(final int id) {
        final GiftCertificate giftCertificate = checkExist(id);
        return mapper.modelToDto(giftCertificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto update(final GiftCertificateDto giftCertificateDto) {
        giftCertificateValidator.updateValidate(giftCertificateDto);
        final GiftCertificate giftCertificate = mapper.dtoToModel(giftCertificateDto);
        checkExist(giftCertificate.getId());
        checkExistByName(giftCertificate.getName());
        giftCertificateRepository.update(giftCertificate);
        return mapper.modelToDto(giftCertificate);
    }

    @Override
    @Transactional
    public void deleteById(final int id) {
        checkExist(id);
        giftCertificateRepository.deleteById(id);
    }

    private GiftCertificate checkExist(final int id) {
        return giftCertificateRepository
                .readOne(id)
                .orElseThrow(() -> new ServiceException("giftCertificate.notFound.id", id));

    }

    private void checkExistByName(final String name) {
        giftCertificateRepository
                .readOneByName(name)
                .ifPresent(giftCertificate -> {
                    throw new ServiceException("giftCertificate.alreadyExists.name", name);
                });
    }
}
