package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TagDtoMapper implements DtoMapper<Tag, TagDto> {

    private final ModelMapper modelMapper;

    @Override
    public TagDto modelToDto(final Tag tag) {
        return modelMapper.map(tag, TagDto.class);

    }

    @Override
    public Tag dtoToModel(final TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }

    @Override
    public Page<TagDto> modelsToDto(Page<Tag> tags) {
        final List<TagDto> collect = tags
                .getContent()
                .stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(collect, tags.getPageable(), tags.getTotalElements());
    }

    @Override
    public List<Tag> dtoToModels(List<TagDto> dtoTags) {
        return dtoTags
                .stream()
                .map(tagDto -> modelMapper.map(tagDto, Tag.class))
                .collect(Collectors.toList());
    }

}
