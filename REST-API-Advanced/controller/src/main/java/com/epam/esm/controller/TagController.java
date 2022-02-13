package com.epam.esm.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping(value = "/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    public ResponseEntity<EntityModel<TagDto>> create(@RequestBody final TagDto tagDto) {
        final TagDto createdTag = tagService.create(tagDto);
        final EntityModel<TagDto> tagDtoEntityModel = linkTagDtoHal(createdTag);
        return new ResponseEntity<>(tagDtoEntityModel, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<TagDto>> readAll() {
        final List<TagDto> dtoTags = tagService.readAll();
        dtoTags.forEach(this::linkTagDto);
        return new ResponseEntity<>(dtoTags, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    public ResponseEntity<EntityModel<TagDto>> readOne(@PathVariable final int id) {
        TagDto tagDto = tagService.readOne(id);
        final EntityModel<TagDto> tagDtoEntityModel = linkTagDtoHal(tagDto);
        return new ResponseEntity<>(tagDtoEntityModel, HttpStatus.OK);
    }

    @GetMapping(value = "/mostUsed", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    public ResponseEntity<EntityModel<TagDto>> readOneMostUsed() {
        final TagDto tagDto = tagService.readOneMostUsed();
        final EntityModel<TagDto> tagDtoEntityModel = linkTagDtoHal(tagDto);
        return new ResponseEntity<>(tagDtoEntityModel, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    public ResponseEntity<EntityModel<TagDto>> deleteById(@PathVariable final int id) {
        final TagDto tagDto = tagService.deleteById(id);
        final EntityModel<TagDto> tagDtoEntityModel = linkTagDtoHal(tagDto);
        return new ResponseEntity<>(tagDtoEntityModel, HttpStatus.OK);
    }

    private EntityModel<TagDto> linkTagDtoHal(final TagDto tagDto) {
        return EntityModel.of(tagDto,
                linkTo(methodOn(TagController.class).readOne(tagDto.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(TagController.class).create(null)))
                        .andAffordance(afford(methodOn(TagController.class).deleteById(tagDto.getId()))));
    }

    private void linkTagDto(final TagDto tagDto) {
        tagDto
                .add(linkTo(methodOn(TagController.class)
                        .readAll())
                        .slash(tagDto.getId())
                        .withSelfRel());
    }
}
