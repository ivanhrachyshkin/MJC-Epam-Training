package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final DtoMapper<Tag, TagDto> mapper;
    private final TagRepository tagRepository;

    public TagServiceImpl(DtoMapper<Tag, TagDto> mapper, TagRepository tagRepository) {
        this.mapper = mapper;
        this.tagRepository = tagRepository;
    }

    @Override
    public TagDto create(final TagDto tagDto) {
//        tagValidator.createValidate(tagDto);
//        final Tag tag = mapper.dtoToModel(tagDto);
//        checkExistByName(tag.getName());
//        final Tag newTag = tagDao.create(tag);
//        return mapper.modelToDto(newTag);
        return null;
    }

    @Override
    public List<TagDto> readAll() {
//        final List<Tag> tags = tagDao.readAll();
//        return mapper.modelsToDto(tags);
        return null;
    }

    @Override
    public TagDto readOne(final int id) {
        final Tag tag = tagRepository.readOne(id);
        return mapper.modelToDto(tag);
    }

    @Override

    public void deleteById(final int id) {
//        checkExist(id);
//        giftCertificateTagDao.deleteGiftCertificateTagByTagId(id);
//        tagDao.deleteById(id);
    }

//    private Tag checkExist(final int id) {
//        return tagDao
//                .readOne(id)
//                .orElseThrow(() -> new ServiceException(rb.getString("tag.notFound.id"), id));
//    }
//
//    private void checkExistByName(final String name) {
//        tagDao
//                .readOneByName(name)
//                .ifPresent(tag -> {
//                    throw new ServiceException(rb.getString("tag.alreadyExists.name"), name);
//                });
//    }
}
