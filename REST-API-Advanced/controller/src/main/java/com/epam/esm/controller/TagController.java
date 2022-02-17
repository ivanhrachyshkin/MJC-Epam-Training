package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tags")
@RequiredArgsConstructor
public class TagController {

    private final HateoasCreator hateoasCreator;
    private final TagService tagService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<TagDto> create(@RequestBody final TagDto tagDto) {
        final TagDto createdTag = tagService.create(tagDto);
        hateoasCreator.linkTagDto(createdTag);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<TagDto>> readAll(@RequestParam(required = false) final Boolean active,
                                            final Integer page,
                                            final Integer size) {
        final List<TagDto> dtoTags = tagService.readAll(active, page, size);
        dtoTags.forEach(hateoasCreator::linkTagDtoOne);
        return new ResponseEntity<>(dtoTags, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> readOne(@PathVariable final int id,
                                       @RequestParam(required = false) final Boolean active) {
        final TagDto tagDto = tagService.readOne(id, active);
        hateoasCreator.linkTagDtoOne(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @GetMapping(value = "/mostUsed", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<TagDto>> readOneMostUsed() {
        final List<TagDto> dtoTags = tagService.readMostUsed();
        dtoTags.forEach(hateoasCreator::linkTagDtoOne);
        return new ResponseEntity<>(dtoTags, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<TagDto> deleteById(@PathVariable final int id) {
        final TagDto tagDto = tagService.deleteById(id);
        hateoasCreator.linkTagDtoOne(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }


}
