package com.epam.esm.controller;

import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping(value = "/gifts")
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificateDto> create(@RequestBody GiftCertificateDto giftCertificateDto) {
        final GiftCertificateDto createdGiftCertificateDto = giftCertificateService.create(giftCertificateDto);
        linkGiftCertificateDto(createdGiftCertificateDto);
        return new ResponseEntity<>(createdGiftCertificateDto, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GiftCertificateDto>> readAll(
            @RequestParam(required = false) final List<String> tags,
            @RequestParam(required = false) final String name,
            @RequestParam(required = false) final String description,
            @RequestParam(required = false) final String dateSort,
            @RequestParam(required = false) final String nameSort) {
        final List<GiftCertificateDto> dtoGiftCertificates
                = giftCertificateService.readAll(tags, name, description, dateSort, nameSort);
        dtoGiftCertificates.forEach(this::linkGiftCertificateDto);
        return new ResponseEntity<>(dtoGiftCertificates, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<GiftCertificateDto> readOne(@PathVariable final int id) {
        final GiftCertificateDto giftCertificateDto = giftCertificateService.readOne(id);
        linkGiftCertificateDto(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<GiftCertificateDto> update(@PathVariable final Integer id,
            @RequestBody final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.setId(id);
        final GiftCertificateDto updatedGiftCertificateDto = giftCertificateService.update(giftCertificateDto);
        linkGiftCertificateDto(giftCertificateDto);
        return new ResponseEntity<>(updatedGiftCertificateDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<GiftCertificateDto> deleteById(@PathVariable int id) {
        final GiftCertificateDto giftCertificateDto = giftCertificateService.deleteById(id);
        linkGiftCertificateDto(giftCertificateDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    private void linkGiftCertificateDto(final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .readOne(giftCertificateDto.getId()))
                        .withSelfRel());

        giftCertificateDto
                .getDtoTags()
                .forEach(this::linkTagDto);
    }

    private void linkTagDto(final TagDto tagDto) {
        tagDto
                .add(linkTo(methodOn(TagController.class)
                        .readOne(tagDto.getId(), null))
                        .withSelfRel().expand());
    }
}
