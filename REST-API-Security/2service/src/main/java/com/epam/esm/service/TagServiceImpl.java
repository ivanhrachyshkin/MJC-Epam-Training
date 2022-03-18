package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.Tag;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.AuthorityValidator;
import com.epam.esm.service.validator.PageValidator;
import com.epam.esm.service.validator.TagValidator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;
    private final DtoMapper<Tag, TagDto> mapper;
    private final TagRepository tagRepository;
    private final TagValidator tagValidator;
    private final PageValidator paginationValidator;
    private final AuthorityValidator authorityValidator;

    @Override
    @Transactional
    public TagDto create(final TagDto tagDto) {
        tagValidator.validate(tagDto);
        final Tag rawTag = mapper.dtoToModel(tagDto);
        rawTag.setIsActive(true);
        final Tag newTag = createOrUpdateOld(rawTag);
        return mapper.modelToDto(newTag);
    }

    @Override
    @Transactional
    public Page<TagDto> readAll(final Pageable pageable) {
        paginationValidator.paginationValidate(pageable);
        final Page<Tag> tags = getTagsByUserRole(pageable);
        return mapper.modelsToDto(tags);
    }

    @Override
    @Transactional
    public TagDto readOne(final int id) {
        tagValidator.validateId(id);
        final Tag tag = getTagByUserRole(id);
        return mapper.modelToDto(tag);
    }

    @Override
    @Transactional
    public Page<TagDto> readMostUsed(final Pageable pageable) {
        final Page<Tag> tag = tagRepository.readMostUsed(pageable);
        return mapper.modelsToDto(tag);
    }

    @Override
    @Transactional
    public void deleteById(final int id) {
        tagValidator.validateId(id);
        Tag tag = checkExistActive(id);
        tag.setIsActive(false);
        tagRepository.save(tag);
    }

    private Page<Tag> getTagsByUserRole(final Pageable pageable) {
        if (authorityValidator.validateAuthorityAdmin()) {
            return tagRepository.findAll(pageable);
        } else {
            return tagRepository.findAllByIsActive(true, pageable);
        }
    }

    private Tag getTagByUserRole(final int id) {
        if (authorityValidator.validateAuthorityAdmin()) {
            return checkExist(id);
        } else {
            return checkExistActive(id);
        }
    }

    private Tag checkExist(final int id) {
        tagValidator.validateId(id);
        return tagRepository
                .findById(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("tag.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getTag(), id));
    }

    private Tag checkExistActive(final int id) {
        tagValidator.validateId(id);
        return tagRepository
                .findByIdAndIsActive(id, true)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("tag.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getTag(), id));
    }

    private Tag createOrUpdateOld(final Tag rawTag) {
        final Optional<Tag> optionalTag = tagRepository.findByName(rawTag.getName());
        if (optionalTag.isPresent() && optionalTag.get().getIsActive()) {
            throw new ServiceException(
                    rb.getString("tag.alreadyExists.name"),
                    HttpStatus.CONFLICT, properties.getTag(), optionalTag.get().getName());
        }
        optionalTag.ifPresent(oldTag -> rawTag.setId(oldTag.getId()));
        return tagRepository.save(rawTag);
    }
}
