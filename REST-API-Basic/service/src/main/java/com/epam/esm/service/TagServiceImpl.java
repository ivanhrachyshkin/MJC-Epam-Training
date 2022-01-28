package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dtomapper.DtoMapper;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final GiftCertificateTagDao giftCertificateTagDao;
    private final TagValidator tagValidator;
    private final DtoMapper<Tag, TagDto> mapper;

    public TagServiceImpl(final TagDao tagDao,
                          final GiftCertificateTagDao giftCertificateTagDao,
                          final TagValidator tagValidator,
                          final DtoMapper<Tag, TagDto> mapper) {
        this.tagDao = tagDao;
        this.giftCertificateTagDao = giftCertificateTagDao;
        this.tagValidator = tagValidator;
        this.mapper = mapper;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @Transactional
    public TagDto create(final TagDto tagDto) {
        tagValidator.createValidate(tagDto);
        final Tag tag = mapper.dtoToModel(tagDto);
        checkExistByName(tag.getName());
        final Tag newTag = tagDao.create(tag);
        return mapper.modelToDto(newTag);
    }

    @Override
    public List<TagDto> readAll() {
        final List<Tag> tags = tagDao.readAll();
        return mapper.modelsToDto(tags);
    }

    @Override
    public TagDto readOne(final int id) {
       final Tag tag = checkExist(id);
        return mapper.modelToDto(tag);
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
