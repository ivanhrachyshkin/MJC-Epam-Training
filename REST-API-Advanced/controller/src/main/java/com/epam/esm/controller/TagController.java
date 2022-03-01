package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TagController {

    private final HateoasCreator hateoasCreator;
    private final TagService tagService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<TagDto> create(@RequestBody final TagDto tagDto) {
        final TagDto createdTag = tagService.create(tagDto);
        hateoasCreator.linkTagDto(createdTag);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @GetMapping
    public HttpEntity<List<TagDto>> readAll(@RequestParam(required = false) final Boolean active,
                                            @RequestParam(required = false) final Integer page,
                                            @RequestParam(required = false) final Integer size) {
        final List<TagDto> dtoTags = tagService.readAll(active, page, size);
        dtoTags.forEach(hateoasCreator::linkTagDtoOne);
        return new ResponseEntity<>(dtoTags, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TagDto> readOne(@PathVariable final int id,
                                          @RequestParam(required = false) final Boolean active) {
        final TagDto tagDto = tagService.readOne(id, active);
        hateoasCreator.linkTagDtoOne(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @GetMapping(value = "/mostUsed")
    public HttpEntity<List<TagDto>> readOneMostUsed() {
        final List<TagDto> dtoTags = tagService.readMostUsed();
        dtoTags.forEach(hateoasCreator::linkTagDtoOne);
        return new ResponseEntity<>(dtoTags, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public HttpEntity<TagDto> deleteById(@PathVariable final int id) {
        final TagDto tagDto = tagService.deleteById(id);
        hateoasCreator.linkTagDtoOne(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }
}
