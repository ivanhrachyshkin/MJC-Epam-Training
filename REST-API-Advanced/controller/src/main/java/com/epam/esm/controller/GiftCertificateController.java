package com.epam.esm.controller;


import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * The intention of controller - handling of the /gift resource.
 */
@RestController
@RequestMapping(value = "/gift")
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    /**
     * The intention of mapping - handling of the creation operation.
     *
     * @param giftCertificateDto the request body representation of the gift certificate.
     * @return the response body representation of the gift certificate.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<GiftCertificateDto> create(@RequestBody GiftCertificateDto giftCertificateDto) {
        final GiftCertificateDto createdGiftCertificateDto = giftCertificateService.create(giftCertificateDto);
        linkGiftCertificateDto(createdGiftCertificateDto);
        return new ResponseEntity<>(createdGiftCertificateDto, HttpStatus.CREATED);
    }

    /**
     * The intention of mapping - handling of the read operation.
     *
     * @param tag         the request param representation of the tag's name.
     * @param name        the request param representation of the gift certificate's name.
     * @param description the request param representation of the gift certificate's description.
     * @param dateSort    the request param representation of sort direction by date.
     * @param nameSort    the request param representation of sort direction by name.
     * @return the response body representation of the gift certificates.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<GiftCertificateDto>> readAll(@RequestParam(required = false) final String tag,
                                                        @RequestParam(required = false) final String name,
                                                        @RequestParam(required = false) final String description,
                                                        @RequestParam(required = false) final Boolean dateSort,
                                                        @RequestParam(required = false) final Boolean nameSort) {
        final List<GiftCertificateDto> dtoGiftCertificates
                = giftCertificateService.readAll(tag, name, description, dateSort, nameSort);

        dtoGiftCertificates.forEach(this::linkGiftCertificateDto);
        return new ResponseEntity<>(dtoGiftCertificates, HttpStatus.OK);
    }

    /**
     * The intention of mapping - handling of the read one operation.
     *
     * @param id the request path variable representation of the gift certificate's id.
     * @return the response body representation of the gift certificate.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<GiftCertificateDto> readOne(@PathVariable final int id) {
        final GiftCertificateDto giftCertificateDto = giftCertificateService.readOne(id);
        linkGiftCertificateDto(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    /**
     * The intention of mapping - handling of the update operation.
     *
     * @param id                 the request path variable representation of the gift certificate's id.
     * @param giftCertificateDto the request body representation of the gift certificate.
     * @return the response body representation of the gift certificate.
     */
    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<GiftCertificateDto> update(@PathVariable final int id,
                                                 @RequestBody final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.setId(id);
        final GiftCertificateDto updatedGiftCertificateDto = giftCertificateService.update(giftCertificateDto);
        linkGiftCertificateDto(giftCertificateDto);
        return new ResponseEntity<>(updatedGiftCertificateDto, HttpStatus.NO_CONTENT);
    }

    /**
     * The intention of mapping - handling of the update operation.
     *
     * @param id the request path variable representation of the gift certificate's id.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int id) {
        giftCertificateService.deleteById(id);
    }

    private void linkTagDto(final TagDto tagDto) {
        tagDto
                .add(linkTo(methodOn(TagController.class)
                        .readAll())
                        .slash(tagDto.getId())
                        .withSelfRel());
    }

    private void linkGiftCertificateDto(final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .readOne(giftCertificateDto.getId()))
                .withSelfRel());

        giftCertificateDto
                .getTags()
                .forEach(this::linkTagDto);
    }
}
