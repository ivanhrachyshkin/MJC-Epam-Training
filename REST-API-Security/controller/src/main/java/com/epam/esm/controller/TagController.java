package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;
import static com.epam.esm.service.dto.RoleDto.Roles.USER;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TagController {

    private final HateoasCreator hateoasCreator;
    private final TagService tagService;

    @Secured({ADMIN})
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody final TagDto tagDto) {
        final TagDto createdTag = tagService.create(tagDto);
        hateoasCreator.linkTagDtoOne(createdTag);
        return createdTag;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<TagDto> readAll(@RequestParam(required = false) final Boolean active,
                                      @PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<TagDto> dtoTags = tagService.readAll(active, pageable);
        return hateoasCreator.linkTagDtos(dtoTags);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDto readOne(@PathVariable final int id) {
        final TagDto tagDto = tagService.readOne(id);
        hateoasCreator.linkTagDtoOne(tagDto);
        return tagDto;
    }

    @GetMapping(value = "/mostUsed")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<TagDto> readOneMostUsed(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<TagDto> dtoTags = tagService.readMostUsed(pageable);
        return hateoasCreator.linkTagDtos(dtoTags);
    }

    @Secured({ADMIN})
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}/admin")
    public ResponseEntity<Void> deleteById(@PathVariable final int id) {
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
