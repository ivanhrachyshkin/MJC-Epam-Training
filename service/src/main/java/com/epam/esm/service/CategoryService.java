package com.epam.esm.service;

import com.epam.esm.service.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> readAll();

    CategoryDto readOne(int id);
}
