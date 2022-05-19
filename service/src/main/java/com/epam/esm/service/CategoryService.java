package com.epam.esm.service;

import com.epam.esm.service.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<CategoryDto> readAll(Pageable pageable);

    CategoryDto readOne(int id);

    CategoryDto readOneByName(String name);
}
