package com.epam.esm.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
    public HttpEntity<List<TagDto>> readAll() {
        final List<TagDto> tags = tagService.readAll();
        tags.forEach(tagDto -> tagDto
                .add(linkTo(methodOn(TagController.class)
                        .readAll())
                        .slash(tagDto.getId())
                        .withSelfRel()));
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    /**
     * The intention of mapping - handling of the read one operation.
     *
     * @param id the request path variable representation of the tags's id.
     * @return
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<TagDto> readOne(@PathVariable final int id) {
        TagDto tagDto = tagService.readOne(id);
        tagDto.add(linkTo(methodOn(TagController.class).readOne(id)).withSelfRel());
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    /**
     * The intention of mapping - handling of the delete operation.
     *
     * @param id the request path variable representation of the tag's id.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable final int id) {
        tagService.deleteById(id);
    }
}
