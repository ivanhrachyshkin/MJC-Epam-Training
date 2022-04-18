package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.Tag;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.AuthorityValidator;
import com.epam.esm.service.validator.PageableValidator;
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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;
    private final DtoMapper<Tag, TagDto> mapper;
    private final TagRepository tagRepository;
    private final TagValidator tagValidator;
    private final PageableValidator paginationValidator;
    private final AuthorityValidator authorityValidator;

    @Override
    @Transactional
    public TagDto create(final TagDto tagDto) {
        tagDto.trim();
        tagValidator.validate(tagDto);
        final Tag rawTag = mapper.dtoToModel(tagDto);
        rawTag.setActive(true);
        final Tag newTag = createOrActiveOld(rawTag);
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
    public TagDto deleteById(final int id) {
        tagValidator.validateId(id);
        final Tag tag = checkExistActive(id);
        tag.setActive(false);
        final Tag deletedTag = tagRepository.save(tag);
        return mapper.modelToDto(deletedTag);
    }

    public void prepareTagsForGiftCertificate(final Set<Tag> rawTags) {
        rawTags.forEach(tag -> {
            tag.setActive(true);
            final Optional<Tag> oldTag = tagRepository.findByName(tag.getName());
            if (oldTag.isPresent()) {
                tag.setId(oldTag.get().getId());
            } else {
                tagRepository.save(tag);
            }
        });
    }

    private Page<Tag> getTagsByUserRole(final Pageable pageable) {
        return authorityValidator.isAdmin()
                ? tagRepository.findAll(pageable)
                : tagRepository.findAllByActive(true, pageable);
    }

    private Tag getTagByUserRole(final int id) {
        return authorityValidator.isAdmin() ? checkExist(id) : checkExistActive(id);
    }

    private Tag checkExist(final int id) {
        return tagRepository
                .findById(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("tag.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getTag(), id));
    }

    private Tag checkExistActive(final int id) {
        return tagRepository
                .findByIdAndActive(id, true)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("tag.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getTag(), id));
    }

    private Tag createOrActiveOld(final Tag rawTag) {
        final Optional<Tag> optionalTag = tagRepository.findByName(rawTag.getName());
        if (optionalTag.isPresent() && optionalTag.get().getActive()) {
            throw new ServiceException(
                    rb.getString("tag.alreadyExists.name"),
                    HttpStatus.CONFLICT, properties.getTag(), optionalTag.get().getName());
        }
        optionalTag.ifPresent(oldTag -> rawTag.setId(oldTag.getId()));
        return tagRepository.save(rawTag);
    }
}
