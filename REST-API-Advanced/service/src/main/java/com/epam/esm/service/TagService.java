package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;

import java.util.List;

public interface TagService {

    public TagDto create(final TagDto tagDto);

    public List<TagDto> readAll(Boolean active);

    public TagDto readOne(int id, Boolean active);

    public TagDto readOneMostUsed();

    public TagDto deleteById(int id);
}
