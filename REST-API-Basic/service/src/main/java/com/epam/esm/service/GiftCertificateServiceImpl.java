package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.validator.CreateGiftCertificateValidator;
import com.epam.esm.service.validator.ReadAllGiftCertificatesValidator;
import com.epam.esm.service.validator.UpdateGiftCertificateValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final GiftCertificateTagDao giftCertificateTagDao;
    private final CreateGiftCertificateValidator createGiftCertificateValidator;
    private final UpdateGiftCertificateValidator updateGiftCertificateValidator;
    private final ReadAllGiftCertificatesValidator readAllGiftCertificatesValidator;

    public GiftCertificateServiceImpl(final GiftCertificateDao giftCertificateDao,
                                      final TagDao tagDao,
                                      final GiftCertificateTagDao giftCertificateTagDao,
                                      final CreateGiftCertificateValidator createGiftCertificateValidator,
                                      final UpdateGiftCertificateValidator updateGiftCertificateValidator,
                                      final ReadAllGiftCertificatesValidator readAllGiftCertificatesValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateTagDao = giftCertificateTagDao;
        this.createGiftCertificateValidator = createGiftCertificateValidator;
        this.updateGiftCertificateValidator = updateGiftCertificateValidator;
        this.readAllGiftCertificatesValidator = readAllGiftCertificatesValidator;
    }

    @Override
    @Transactional
    public GiftCertificate create(final GiftCertificate giftCertificate) {
        createGiftCertificateValidator.validate(giftCertificate);
        checkExistByName(giftCertificate.getName());
        final GiftCertificate newGiftCertificate = giftCertificateDao.create(giftCertificate);
        createOrAssignTags(newGiftCertificate);
        return newGiftCertificate;
    }

    @Override
    public List<GiftCertificate> readAll(final String tag,
                                         final String name,
                                         final String description,
                                         final Boolean asc) {
        readAllGiftCertificatesValidator.validate(tag, name, description);
        return giftCertificateDao.readAll(tag, name, description, asc);
    }

    @Override
    public GiftCertificate readOne(final int id) {
        return checkExist(id);
    }

    @Override
    @Transactional
    public GiftCertificate update(final GiftCertificate giftCertificate) {
        updateGiftCertificateValidator.validate(giftCertificate);
        checkExist(giftCertificate.getId());
        checkExistByName(giftCertificate.getName());
        giftCertificateDao.update(giftCertificate);
        createOrAssignTags(giftCertificate);
        return giftCertificate;
    }

    @Override
    @Transactional
    public void deleteById(final int id) {
        checkExist(id);
        giftCertificateDao.deleteById(id);
        giftCertificateTagDao.deleteGiftCertificateTagByCertificateId(id);
    }

    private GiftCertificate checkExist(final int id) {
        return giftCertificateDao
                .readOne(id)
                .orElseThrow(() -> new ServiceException("Gift certificate with id = %s not found", id));

    }

    private void checkExistByName(final String name) {
        giftCertificateDao
                .readOneByName(name)
                .ifPresent(giftCertificate -> {
                    throw new ServiceException("Gift certificate with name = %s is already exist", name);
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
