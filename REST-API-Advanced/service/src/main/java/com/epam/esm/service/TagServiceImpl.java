package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.TagValidator;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
        final Tag rawTag = mapper.dtoToModel(tagDto);
        final Tag newTag = createOrUpdateOld(rawTag);
        return mapper.modelToDto(newTag);
    }

    @Override
    @Transactional
    public List<TagDto> readAll(final Boolean active) {
        final List<Tag> tags = tagRepository.readAll(active);
        return mapper.modelsToDto(tags);
    }

    @Override
    @Transactional
    public TagDto readOne(final int id, final Boolean active) {
        Tag tag = checkExist(id, active);
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
        checkExist(id, true);
        final Tag tag = tagRepository.deleteById(id);
        return mapper.modelToDto(tag);
    }

    private Tag checkExist(final int id, final Boolean active) {
        return tagRepository
                .readOne(id, active)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("tag.notFound.id"), HttpStatus.NOT_FOUND, POSTFIX, id));
    }

    private Tag createOrUpdateOld(final Tag rawTag) {
        final Optional<Tag> optionalTag = tagRepository.readOneByName(rawTag.getName());
        Tag newTag;
        if (optionalTag.isPresent()) {
            if (!optionalTag.get().getActive()) {
                rawTag.setId(optionalTag.get().getId());
                rawTag.setActive(true);
                newTag = tagRepository.update(rawTag);
            } else {
                throw new ServiceException(
                        rb.getString("tag.alreadyExists.name"),
                        HttpStatus.CONFLICT, POSTFIX, optionalTag.get().getName());
            }
        } else {
            rawTag.setId(null);
            newTag = tagRepository.create(rawTag);
        }
        return newTag;
    }
}
