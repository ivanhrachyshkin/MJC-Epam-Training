package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;

import java.util.List;

public interface TagService {

    TagDto create(final TagDto tagDto);

    List<TagDto> readAll(Boolean active,
                         Integer page,
                         Integer size);

    TagDto readOne(int id, Boolean active);

    List<TagDto> readMostUsed();

    TagDto deleteById(int id);
}
