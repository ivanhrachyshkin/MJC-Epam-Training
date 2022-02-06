package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.TagValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final ResourceBundle rb;
    private final TagRepository tagRepository;
    private final TagValidator tagValidator;
    private final DtoMapper<Tag, TagDto> mapper;

    @Override
    @Transactional
    public TagDto create(final TagDto tagDto) {
        tagValidator.createValidate(tagDto);
        final Tag tag = mapper.dtoToModel(tagDto);
        checkExistByName(tag.getName());
        final Tag newTag = tagRepository.create(tag);
        return mapper.modelToDto(newTag);
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
        tagRepository.deleteById(id);
    }

    private Tag checkExist(final int id) {
        return tagRepository
                .readOne(id)
                .orElseThrow(() -> new ServiceException(rb.getString("tag.notFound.id"), id));
    }

    private void checkExistByName(final String name) {
        tagRepository
                .readOneByName(name)
                .ifPresent(tag -> {
                    throw new ServiceException(rb.getString("tag.alreadyExists.name"), name);
                });
    }
}
