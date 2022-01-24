package com.epam.esm.controller.mapper;

import com.epam.esm.model.Tag;
import com.epam.esm.controller.dto.TagDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagDtoMapper implements DtoMapper<Tag, TagDto> {

    private final ModelMapper modelMapper;

    public TagDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDto modelToDto(final Tag tag) {
        final TagDto tagDto = modelMapper.map(tag, TagDto.class);
        return tagDto;

    }

    @Override
    public Tag dtoToModel(final TagDto tagDto) {
        final Tag tag = modelMapper.map(tagDto, Tag.class);
        return tag;
    }

    @Override
    public List<TagDto> modelsToDto(List<Tag> tags) {
        return tags
                .stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public List<Tag> dtoToModels(List<TagDto> tagDtos) {
        return tagDtos
                .stream()
                .map(tagDto -> modelMapper.map(tagDtos, Tag.class))
                .collect(Collectors.toList());
    }

}
