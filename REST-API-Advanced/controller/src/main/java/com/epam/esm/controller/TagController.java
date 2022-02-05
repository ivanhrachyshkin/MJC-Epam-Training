package com.epam.esm.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * The intention of mapping - handling of the creation operation.
     *
     * @param tagDto the request body representation of the tag.
     * @return the response body representation of the tag.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<TagDto> create(@RequestBody final TagDto tagDto) {
        final TagDto createdTag = tagService.create(tagDto);
        linkTagDto(createdTag);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    /**
     * The intention of mapping - handling of the read operation.
     *
     * @return the response body representation of the tags.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<TagDto>> readAll() {
        final List<TagDto> dtoTags = tagService.readAll();
        dtoTags.forEach(this::linkTagDto);
        return new ResponseEntity<>(dtoTags, HttpStatus.OK);
    }

    /**
     * The intention of mapping - handling of the read one operation.
     *
     * @param id the request path variable representation of the tags's id.
     * @return
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<TagDto> readOne(@PathVariable final int id) {
        final TagDto tagDto = tagService.readOne(id);
        linkTagDto(tagDto);
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

    private void linkTagDto(final TagDto tagDto) {
        tagDto
                .add(linkTo(methodOn(TagController.class)
                        .readAll())
                        .slash(tagDto.getId())
                        .withSelfRel());
    }
}
