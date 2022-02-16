package com.epam.esm.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.Affordances;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<TagDto> create(@RequestBody final TagDto tagDto) {
        final TagDto createdTag = tagService.create(tagDto);
        linkTagDto(createdTag);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<TagDto>> readAll(@RequestParam(required = false) final Boolean active,
                                            final Integer page,
                                            final Integer size) {
        final List<TagDto> dtoTags = tagService.readAll(active, page, size);
        dtoTags.forEach(this::linkTagDto);
        return new ResponseEntity<>(dtoTags, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> readOne(@PathVariable final int id,
                                       @RequestParam(required = false) final Boolean active) {
        final TagDto tagDto = tagService.readOne(id, active);
        linkTagDto(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @GetMapping(value = "/mostUsed", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<TagDto> readOneMostUsed() {
        final TagDto tagDto = tagService.readOneMostUsed();
        linkTagDto(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<TagDto> deleteById(@PathVariable final int id) {
        final TagDto tagDto = tagService.deleteById(id);
        linkTagDto(tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    private void linkTagDto(final TagDto tagDto) {
        tagDto
                .add(linkTo(methodOn(TagController.class)
                        .readOne(tagDto.getId(), null))
                        .withSelfRel().expand());
    }
}
