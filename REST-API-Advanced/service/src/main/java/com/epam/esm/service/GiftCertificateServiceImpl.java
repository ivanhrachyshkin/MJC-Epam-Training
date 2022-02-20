package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.SortValidator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;
    private final DtoMapper<GiftCertificate, GiftCertificateDto> mapper;
    private final GiftCertificateRepository giftCertificateRepository;
    private final GiftCertificateValidator giftCertificateValidator;
    private final SortValidator sortValidator;
    private final PaginationValidator paginationValidator;

    @Override
    @Transactional
    public GiftCertificateDto create(final GiftCertificateDto giftCertificateDto) {
        giftCertificateValidator.createValidate(giftCertificateDto);
        checkExistByName(giftCertificateDto.getName());
        final GiftCertificate rawGiftCertificate = mapper.dtoToModel(giftCertificateDto);
        final GiftCertificate newGiftCertificate = giftCertificateRepository.create(rawGiftCertificate);
        return mapper.modelToDto(newGiftCertificate);
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> readAll(final List<String> tags,
                                            final String name,
                                            final String description,
                                            final String dateSort,
                                            final String nameSort,
                                            final Integer page,
                                            final Integer size) {
        giftCertificateValidator.readAllValidate(tags, name, description);
        paginationValidator.paginationValidate(page, size);
        sortValidator.sortValidate(dateSort);
        sortValidator.sortValidate(nameSort);
        List<GiftCertificate> giftCertificates
                = giftCertificateRepository.readAll(tags, name, description, dateSort, nameSort, page, size);
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
        checkExistByName(giftCertificateDto.getName());
        final GiftCertificate rawGiftCertificate = mapper.dtoToModel(giftCertificateDto);
        final GiftCertificate oldGiftCertificate = checkExist(rawGiftCertificate.getId());
        rawGiftCertificate.setCreateDate(oldGiftCertificate.getCreateDate());
        final GiftCertificate updatedGiftCertificate = giftCertificateRepository.update(rawGiftCertificate);
        return mapper.modelToDto(updatedGiftCertificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto deleteById(final int id) {
        checkExist(id);
        final GiftCertificate giftCertificate = giftCertificateRepository.deleteById(id);
        return mapper.modelToDto(giftCertificate);
    }

    private GiftCertificate checkExist(final int id) {
        return giftCertificateRepository
                .readOne(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("giftCertificate.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getGift(), id));
    }

    private void checkExistByName(final String name) {
        giftCertificateRepository
                .readOneByName(name)
                .ifPresent(giftCertificate -> {
                    throw new ServiceException(
                            rb.getString("giftCertificate.alreadyExists.name"),
                            HttpStatus.CONFLICT, properties.getGift(), name);
                });
    }

}
