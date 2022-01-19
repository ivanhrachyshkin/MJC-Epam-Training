package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import com.epam.esm.validator.CreateTagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
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
        final Integer newId = tagDao.create(tag);
        tag.setId(newId);
        return tag;
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
        final Tag tag = tagDao.readOne(id);
        if (tag == null) {
            throw new NotFoundException("Tag with id = %s not found", id);
        }
        return tag;
    }

    private void checkExistByName(final String name) {
        final Tag tag = tagDao.readOneByName(name);
        if (tag != null) {
            throw new AlreadyExistException("Tag with name = %s is already exist", name);
        }
    }
}
