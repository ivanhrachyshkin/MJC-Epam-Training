package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import com.epam.esm.service.validator.CreateTagValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private TagDao tagDao;
    private GiftCertificateTagDao giftCertificateTagDao;
    private CreateTagValidator createTagValidator;

    public TagServiceImpl(final TagDao tagDao,
                          final GiftCertificateTagDao giftCertificateTagDao,
                          final CreateTagValidator createTagValidator) {
        this.tagDao = tagDao;
        this.giftCertificateTagDao = giftCertificateTagDao;
        this.createTagValidator = createTagValidator;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @Transactional
    public Tag create(final Tag tag) {
        createTagValidator.validate(tag);
        checkExistByName(tag.getName());
        final Tag newTag = tagDao.create(tag);
        return newTag;
    }

    @Override
    public List<Tag> readAll() {
        return tagDao.readAll();
    }

    @Override
    public Tag readOne(final int id) {
        return checkExist(id);
    }

    @Override
    @Transactional
    public void deleteById(final int id) {
        checkExist(id);
        giftCertificateTagDao.deleteGiftCertificateTagByTagId(id);
        tagDao.deleteById(id);
    }

    private Tag checkExist(final int id) {
        return tagDao
                .readOne(id)
                .orElseThrow(() -> new ServiceException("Tag with id = %s not found", id));
    }

    private void checkExistByName(final String name) {
        tagDao
                .readOneByName(name)
                .ifPresent(tag -> {
                    throw new ServiceException("Tag with name = %s is already exist", name);
                });
    }
}
