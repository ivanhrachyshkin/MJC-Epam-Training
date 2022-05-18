package com.epam.esm.service;

import com.epam.esm.dao.CategoryRepository;
import com.epam.esm.model.Category;
import com.epam.esm.service.dto.CategoryDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final DtoMapper<Category, CategoryDto> mapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> readAll() {
        final Page<Category> categories = categoryRepository.findAll(Pageable.unpaged());
        return categories.stream().map(mapper::modelToDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto readOne(int id) {
        return mapper.modelToDto(categoryRepository.findById(id).get());
    }
}
