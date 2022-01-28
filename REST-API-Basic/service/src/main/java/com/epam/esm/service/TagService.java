package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;

import java.util.List;

public interface TagService {

    TagDto create(TagDto tagDto);

    List<TagDto> readAll();

    TagDto readOne(int id);

    void deleteById(int id);
}
