package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
                .map(tagDto -> modelMapper.map(tagDto, Tag.class))
                .collect(Collectors.toList());
    }

}
