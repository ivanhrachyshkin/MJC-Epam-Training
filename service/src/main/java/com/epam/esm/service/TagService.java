package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {

    TagDto create(final TagDto tagDto);

    Page<TagDto> readAll(Pageable pageable);

    TagDto readOne(int id);

    Page<TagDto> readMostUsed(Pageable pageable);

    TagDto deleteById(int id);
}
