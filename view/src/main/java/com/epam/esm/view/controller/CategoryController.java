package com.epam.esm.view.controller;

import com.epam.esm.service.CategoryService;
import com.epam.esm.service.dto.CategoryDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.view.hateoas.HateoasCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = {"http://192.168.43.65:3000"})
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CategoryController {

    private final HttpServletResponse response;
    private final HateoasCreator hateoasCreator;
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<CategoryDto> readAll(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<CategoryDto> dtoCategories = categoryService.readAll(pageable);
        return hateoasCreator.linkCategoryDtos(dtoCategories);
    }

    @GetMapping(value = {"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto readOne(@PathVariable final int id) {
        final CategoryDto dtoCategory = categoryService.readOne(id);
        setLocationHeader(dtoCategory);
        return hateoasCreator.linkCategoryDto(dtoCategory);
    }

    private void setLocationHeader(final CategoryDto categoryDto) {
        final String href = linkTo(methodOn(CategoryController.class)
                .readOne(categoryDto.getId()))
                .withSelfRel().getHref();
        response.addHeader(HttpHeaders.LOCATION, href);
    }

}
