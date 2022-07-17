package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.Category;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.CategoryDto;
import com.epam.esm.service.dto.OrderDto;
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
public class CategoryDtoMapper implements DtoMapper<Category, CategoryDto> {

    private final ModelMapper modelMapper;

    @Override
    public CategoryDto modelToDto(final Category category) {
        return modelMapper.map(category, CategoryDto.class);

    }

    @Override
    public Category dtoToModel(final CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    @Override
    public Page<CategoryDto> modelsToDto(Page<Category> categories) {
        List<CategoryDto> collect = categories
                .stream()
                .map(this::modelToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(collect, categories.getPageable(), categories.getTotalElements());
    }
}
