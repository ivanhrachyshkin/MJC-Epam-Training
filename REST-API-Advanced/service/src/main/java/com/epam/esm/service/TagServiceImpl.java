package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.TagValidator;
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
    private final DtoMapper<Tag, TagDto> mapper;
    private final TagRepository tagRepository;
    private final TagValidator tagValidator;
    private final PaginationValidator paginationValidator;

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
    public List<TagDto> readAll(final Boolean active, final Integer page, final Integer size) {
        paginationValidator.paginationValidate(page, size);
        final List<Tag> tags = tagRepository.readAll(active, page, size);
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
    public List<TagDto> readMostUsed() {
        final List<Tag> tag = tagRepository.readMostUsed();
        return mapper.modelsToDto(tag);
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
        if (optionalTag.isPresent() && optionalTag.get().getActive()) {
            throw new ServiceException(
                    rb.getString("tag.alreadyExists.name"),
                    HttpStatus.CONFLICT, POSTFIX, optionalTag.get().getName());
        }
        optionalTag.ifPresent(oldTag -> rawTag.setId(oldTag.getId()));
        return tagRepository.create(rawTag);
    }
}
