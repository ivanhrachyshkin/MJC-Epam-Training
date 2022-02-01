package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    @Transactional
    public List<TagDto> readAll() {
        final List<Tag> tags = tagRepository.readAll();
        return mapper.modelsToDto(tags);
    }

    @Override
    @Transactional
    public TagDto readOne(final int id) {
        Tag tag = checkExist(id);
        return mapper.modelToDto(tag);
    }

    @Override
    @Transactional
    public void deleteById(final int id) {
        checkExist(id);
        //giftCertificateTagDao.deleteGiftCertificateTagByTagId(id);
        tagRepository.deleteById(id);
    }

    private Tag checkExist(final int id) {
        return tagRepository
                .readOne(id)
                .orElseThrow(() -> new ServiceException("tag.notFound.id", id));
    }

    private void checkExistByName(final String name) {
        tagRepository
                .readOneByName(name)
                .ifPresent(tag -> {
                    throw new ServiceException("tag.alreadyExists.name", name);
                });
    }
}
