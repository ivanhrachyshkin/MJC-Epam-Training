package com.epam.esm.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The intention of controller - handling of the /tag resource.
 */
@RestController
@RequestMapping(value = "/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * The intention of mapping - handling of the creation operation.
     *
     * @param tagDto the request body representation of the tag.
     * @return the response body representation of the tag.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody final TagDto tagDto) {
        return tagService.create(tagDto);
    }

    /**
     * The intention of mapping - handling of the read operation.
     *
     * @return the response body representation of the tags.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> readAll() {
        return tagService.readAll();
    }

    /**
     * The intention of mapping - handling of the read one operation.
     *
     * @param id the request path variable representation of the tags's id.
     * @return
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public TagDto readOne(@PathVariable final int id) {
        return tagService.readOne(id);
    }

    /**
     * The intention of mapping - handling of the delete operation.
     * @param id the request path variable representation of the tag's id.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable final int id) {
        tagService.deleteById(id);
    }
}
