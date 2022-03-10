package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.GiftCertificateSpecification;
import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.PageValidator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import static com.epam.esm.dao.GiftCertificateSpecification.giftCertificateFiltered;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    @Setter
    private ResourceBundle rb;
    private final Clock clock;
    private final ExceptionStatusPostfixProperties properties;
    private final DtoMapper<GiftCertificate, GiftCertificateDto> mapper;
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final GiftCertificateValidator giftCertificateValidator;
    private final PageValidator paginationValidator;

    @Override
    @Transactional
    public GiftCertificateDto create(final GiftCertificateDto giftCertificateDto) {
        giftCertificateValidator.createValidate(giftCertificateDto);
        checkExistByName(giftCertificateDto.getName());
        final GiftCertificate rawGiftCertificate = mapper.dtoToModel(giftCertificateDto);
        rawGiftCertificate.setCreateDate(LocalDateTime.now(clock));
        rawGiftCertificate.setLastUpdateDate(LocalDateTime.now(clock));
        setTagId(rawGiftCertificate.getTags());
        final GiftCertificate newGiftCertificate = giftCertificateRepository.save(rawGiftCertificate);
        return mapper.modelToDto(newGiftCertificate);
    }

    @Override
    @Transactional
    public Page<GiftCertificateDto> readAll(final List<String> tags,
                                            final GiftCertificateRequestParamsContainer container,
                                            final Pageable pageable) {
        giftCertificateValidator.readAllValidate(tags, container);
        paginationValidator.paginationValidate(pageable);
        final String giftCertificateName = container.getName();
        final String giftCertificateDescription = container.getDescription();
        final Page<GiftCertificate> giftCertificates = giftCertificateRepository
                .findAll(giftCertificateFiltered(tags, giftCertificateName, giftCertificateDescription), pageable);
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
        rawGiftCertificate.setLastUpdateDate(LocalDateTime.now(clock));
        setTagId(oldGiftCertificate.getTags());
        final GiftCertificate updatedGiftCertificate = giftCertificateRepository.save(rawGiftCertificate);
        return mapper.modelToDto(updatedGiftCertificate);
    }

    @Override
    @Transactional
    public void deleteById(final int id) {
        giftCertificateValidator.validateId(id);
        checkExist(id);
        giftCertificateRepository.deleteById(id);
    }

    private void setTagId(final Set<Tag> tags) {
        tags.forEach(tag -> {
            tag.setActive(true);
            final Optional<Tag> oldTag = tagRepository.findByName(tag.getName());
            if (oldTag.isPresent()) {
                tag.setId(oldTag.get().getId());
            } else {
                tagRepository.save(tag);
            }
        });
    }

    private GiftCertificate checkExist(final int id) {
        return giftCertificateRepository
                .findById(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("giftCertificate.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getGift(), id));
    }

    private void checkExistByName(final String name) {
        giftCertificateRepository
                .findByName(name)
                .ifPresent(giftCertificate -> {
                    throw new ServiceException(
                            rb.getString("giftCertificate.alreadyExists.name"),
                            HttpStatus.CONFLICT, properties.getGift(), name);
                });
    }
}
