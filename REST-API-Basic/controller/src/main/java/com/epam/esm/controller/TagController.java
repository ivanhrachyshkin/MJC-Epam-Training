package com.epam.esm.controller;

import com.epam.esm.controller.mapper.DtoMapper;
import com.epam.esm.model.Tag;
import com.epam.esm.controller.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tag")
public class TagController {

    private final TagService tagService;
    private final DtoMapper<Tag, TagDto> mapper;

    public TagController(TagService tagService, DtoMapper<Tag, TagDto> mapper) {
        this.tagService = tagService;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody final TagDto tagDto) {
        final Tag tag = mapper.dtoToModel(tagDto);
        final Tag createdTag = tagService.create(tag);
        return mapper.modelToDto(createdTag);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> readAll() {
        final List<Tag> tags = tagService.readAll();
        return mapper.modelsToDto(tags);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public TagDto readOne(@PathVariable final int id) {
        final Tag tag = tagService.readOne(id);
        return mapper.modelToDto(tag);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable final int id) {
        tagService.deleteById(id);
    }
}
