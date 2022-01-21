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

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final GiftCertificateTagDao giftCertificateTagDao;
    private final Clock clock;
    private final CreateGiftCertificateValidator createGiftCertificateValidator;
    private final UpdateGiftCertificateValidator updateGiftCertificateValidator;
    private final ReadAllGiftCertificatesValidator readAllGiftCertificatesValidator;

    public GiftCertificateServiceImpl(final GiftCertificateDao giftCertificateDao,
                                      final TagDao tagDao,
                                      final GiftCertificateTagDao giftCertificateTagDao,
                                      final Clock clock,
                                      final CreateGiftCertificateValidator createGiftCertificateValidator,
                                      final UpdateGiftCertificateValidator updateGiftCertificateValidator,
                                      final ReadAllGiftCertificatesValidator readAllGiftCertificatesValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateTagDao = giftCertificateTagDao;
        this.clock = clock;
        this.createGiftCertificateValidator = createGiftCertificateValidator;
        this.updateGiftCertificateValidator = updateGiftCertificateValidator;
        this.readAllGiftCertificatesValidator = readAllGiftCertificatesValidator;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @Transactional
    public GiftCertificate create(final GiftCertificate giftCertificate) {
        createGiftCertificateValidator.validate(giftCertificate);
        checkExistByName(giftCertificate.getName());
        giftCertificate.setCreateDate(LocalDateTime.now(clock));
        giftCertificate.setLastUpdateDate(LocalDateTime.now(clock));
        final Integer newId = giftCertificateDao.create(giftCertificate);
        giftCertificate.setId(newId);
        createOrAssignTags(giftCertificate);
        return giftCertificate;
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
        giftCertificate.setLastUpdateDate(LocalDateTime.now(clock));
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
        final GiftCertificate giftCertificate = giftCertificateDao.readOne(id);
        if (giftCertificate == null) {
            throw new NotFoundException("Gift certificate with id = %s not found", id);
        }
        return giftCertificate;
    }

    private void checkExistByName(final String name) {
        final GiftCertificate giftCertificate = giftCertificateDao.readOneByName(name);
        if (giftCertificate != null) {
            throw new AlreadyExistException("Gift certificate with name = %s is already exist", name);
        }
    }

    //todo not null -> set
    @SuppressWarnings("ConstantConditions")
    private void createOrAssignTags(final GiftCertificate giftCertificate) {
        final Set<Tag> tags = giftCertificate.getTags();
        if (tags != null) {
            final Set<Tag> assignedTags = tags
                    .stream()
                    .filter(Objects::nonNull)
                    .peek(tag -> {
                        final Tag oldTag = tagDao.readOneByName(tag.getName());
                        if (oldTag == null) {
                            final Integer newId = tagDao.create(tag);
                            tag.setId(newId);
                        } else {
                            tag.setId(oldTag.getId());
                        }
                        giftCertificateTagDao.createGiftCertificateTag(giftCertificate.getId(), tag.getId());
                    })
                    .collect(Collectors.toSet());
            giftCertificate.setTags(assignedTags);
            //   }
        }
    }
}
