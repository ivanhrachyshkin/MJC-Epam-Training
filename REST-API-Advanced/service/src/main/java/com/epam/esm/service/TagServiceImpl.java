package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.TagValidator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private static final String POSTFIX = "02";

    @Setter
    private ResourceBundle rb;
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
    public TagDto readOneMostUsed() {
        final Tag tag = tagRepository.readOneMostUsed()
                .orElseThrow(() -> new ServiceException(rb.getString("tag.no"), HttpStatus.NOT_FOUND, POSTFIX));
        return mapper.modelToDto(tag);
    }

    @Override
    @Transactional
    public TagDto deleteById(final int id) {
        checkExist(id);
        final Tag tag = tagRepository.deleteById(id);
        return mapper.modelToDto(tag);
    }

    private Tag checkExist(final int id) {
        return tagRepository
                .readOne(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("tag.notFound.id"), HttpStatus.NOT_FOUND, POSTFIX, id));
    }

    private void checkExistByName(final String name) {
        tagRepository
                .readOneByName(name)
                .ifPresent(tag -> {
                    throw new ServiceException(
                            rb.getString("tag.alreadyExists.name"), HttpStatus.NOT_FOUND, POSTFIX, name);
                });
    }
}
