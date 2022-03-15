package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.controller.security.jwt.JwtUser;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;
import static com.epam.esm.service.dto.RoleDto.Roles.USER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TagController {

    private final HttpServletResponse response;
    private final HateoasCreator hateoasCreator;
    private final TagService tagService;

    @Secured({ADMIN})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody final TagDto tagDto) {
        final TagDto createdTag = tagService.create(tagDto);
        setLocationHeader(createdTag);
        return hateoasCreator.linkTagDtoOne(createdTag);
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
        return hateoasCreator.linkTagDtoOne(tagDto);
    }

    @GetMapping(value = "/mostUsed")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<TagDto> readOneMostUsed(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<TagDto> dtoTags = tagService.readMostUsed(pageable);
        return hateoasCreator.linkTagDtos(dtoTags);
    }

    @Secured({ADMIN})
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable final int id) {
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void setLocationHeader(final TagDto tagDto) {
        final String href = linkTo(methodOn(TagController.class)
                .readOne(tagDto.getId()))
                .withSelfRel().getHref();
        response.addHeader(HttpHeaders.LOCATION, href);
    }
}
