package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.SortValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final ResourceBundle rb;
    private final DtoMapper<GiftCertificate, GiftCertificateDto> mapper;
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final GiftCertificateTagDao giftCertificateTagDao;
    private final GiftCertificateValidator giftCertificateValidator;
    private final SortValidator sortValidator;

    public GiftCertificateServiceImpl(final ResourceBundle rb,
                                      final DtoMapper<GiftCertificate, GiftCertificateDto> mapper,
                                      final GiftCertificateDao giftCertificateDao,
                                      final TagDao tagDao,
                                      final GiftCertificateTagDao giftCertificateTagDao,
                                      final GiftCertificateValidator giftCertificateValidator,
                                      final SortValidator sortValidator) {
        this.rb = rb;
        this.mapper = mapper;
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateTagDao = giftCertificateTagDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.sortValidator = sortValidator;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(final GiftCertificateDto giftCertificateDto) {
        giftCertificateValidator.createValidate(giftCertificateDto);
        final GiftCertificate giftCertificate = mapper.dtoToModel(giftCertificateDto);
        checkExistByName(giftCertificate.getName());
        final GiftCertificate newGiftCertificate = giftCertificateDao.create(giftCertificate);
        createOrAssignTags(newGiftCertificate);
        return mapper.modelToDto(newGiftCertificate);
    }

    @Override
    public List<GiftCertificateDto> readAll(final String tag,
                                            final String name,
                                            final String description,
                                            final String dateSort,
                                            final String nameSort) {
        giftCertificateValidator.readAllValidate(tag, name, description);
        sortValidator.sortValidate(dateSort);
        sortValidator.sortValidate(nameSort);
        List<GiftCertificate> giftCertificates
                = giftCertificateDao.readAll(tag, name, description, dateSort, nameSort);
        return mapper.modelsToDto(giftCertificates);
    }

    @Override
    public GiftCertificateDto readOne(final int id) {
        GiftCertificate giftCertificate = checkExist(id);
        return mapper.modelToDto(giftCertificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto update(final GiftCertificateDto giftCertificateDto) {
        giftCertificateValidator.updateValidate(giftCertificateDto);
        final GiftCertificate giftCertificate = mapper.dtoToModel(giftCertificateDto);
        checkExist(giftCertificate.getId());
        checkExistByName(giftCertificate.getName());
        giftCertificateDao.update(giftCertificate);
        createOrAssignTags(giftCertificate);
        return mapper.modelToDto(giftCertificate);
    }

    @Override
    @Transactional
    public void deleteById(final int id) {
        checkExist(id);
        giftCertificateTagDao.deleteGiftCertificateTagByCertificateId(id);
        giftCertificateDao.deleteById(id);
    }

    private GiftCertificate checkExist(final int id) {
        return giftCertificateDao
                .readOne(id)
                .orElseThrow(() -> new ServiceException(rb.getString("giftCertificate.notFound.id"), id));

    }

    private void checkExistByName(final String name) {
        giftCertificateDao
                .readOneByName(name)
                .ifPresent(giftCertificate -> {
                    throw new ServiceException(rb.getString("giftCertificate.alreadyExists.name"), name);
                });
    }

    private void createOrAssignTags(final GiftCertificate giftCertificate) {
        final Set<Tag> assignedTags = giftCertificate
                .getTags()
                .stream()
                .filter(Objects::nonNull)
                .peek(tag -> createOrAssignTags(tag, giftCertificate))
                .collect(Collectors.toSet());
        giftCertificate.setTags(assignedTags);
    }

    private void createOrAssignTags(final Tag tag, final GiftCertificate giftCertificate) {
        final Optional<Tag> oldTag = tagDao.readOneByName(tag.getName());
        if (!oldTag.isPresent()) {
            final Tag newTag = tagDao.create(tag);
            tag.setId(newTag.getId());
        } else {
            tag.setId(oldTag.get().getId());
        }
        giftCertificateTagDao.createGiftCertificateTag(giftCertificate.getId(), tag.getId());
    }
}
