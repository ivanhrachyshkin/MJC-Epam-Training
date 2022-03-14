package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.esm.service.dto.RoleDto.Roles.ADMIN;
import static com.epam.esm.service.dto.RoleDto.Roles.ROLE_ADMIN;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/gifts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class GiftCertificateController {

    private final HateoasCreator hateoasCreator;
    private final GiftCertificateService giftCertificateService;

    @Secured(ADMIN)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificateDto> create(@RequestBody GiftCertificateDto giftCertificateDto) {
        final GiftCertificateDto createdGiftCertificateDto = giftCertificateService.create(giftCertificateDto);
        hateoasCreator.linkGiftCertificateDtoOne(createdGiftCertificateDto);
        createdGiftCertificateDto
                .getDtoTags()
                .forEach(hateoasCreator::linkTagDto);
        final HttpHeaders httpHeaders = setLocationHeader(createdGiftCertificateDto);
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(createdGiftCertificateDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<GiftCertificateDto> readAll(@RequestParam(required = false) final List<String> tags,
                                                  final GiftCertificateRequestParamsContainer container,
                                                  @PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<GiftCertificateDto> dtoGiftCertificates
                = giftCertificateService.readAll(tags, container, pageable);
        dtoGiftCertificates.forEach(giftCertificateDto -> {
            giftCertificateDto.getDtoTags().forEach(hateoasCreator::linkTagDto);
        });
        return hateoasCreator.linkGiftCertificateDtos(dtoGiftCertificates);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto readOne(@PathVariable final int id) {
        final GiftCertificateDto giftCertificateDto = giftCertificateService.readOne(id);
        hateoasCreator.linkGiftCertificateDtoOne(giftCertificateDto);
        giftCertificateDto.getDtoTags().forEach(hateoasCreator::linkTagDto);
        return giftCertificateDto;
    }

    @Secured(ADMIN)
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto update(@PathVariable final Integer id,
                                     @RequestBody final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.setId(id);
        final GiftCertificateDto updatedGiftCertificateDto = giftCertificateService.update(giftCertificateDto);
        hateoasCreator.linkGiftCertificateDtoOne(giftCertificateDto);
        updatedGiftCertificateDto.getDtoTags().forEach(hateoasCreator::linkTagDto);
        return giftCertificateDto;
    }

    @Secured(ADMIN)
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        giftCertificateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private HttpHeaders setLocationHeader(final GiftCertificateDto giftCertificateDto) {
        final String href = linkTo(methodOn(GiftCertificateController.class)
                .readOne(giftCertificateDto.getId()))
                .withSelfRel().getHref();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.LOCATION, href);
        return httpHeaders;
    }
}
