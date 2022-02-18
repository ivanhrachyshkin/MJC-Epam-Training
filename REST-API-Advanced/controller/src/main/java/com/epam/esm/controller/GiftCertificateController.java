package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasCreator;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/gifts")
@RequiredArgsConstructor
public class GiftCertificateController {

    private final HateoasCreator hateoasCreator;
    private final GiftCertificateService giftCertificateService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificateDto> create(@RequestBody GiftCertificateDto giftCertificateDto) {
        final GiftCertificateDto createdGiftCertificateDto = giftCertificateService.create(giftCertificateDto);
        hateoasCreator.linkGiftCertificateDtoOne(createdGiftCertificateDto);
        createdGiftCertificateDto
                .getDtoTags()
                .forEach(hateoasCreator::linkTagDto);
        return new ResponseEntity<>(createdGiftCertificateDto, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GiftCertificateDto>> readAll(
            @RequestParam(required = false) final List<String> tags,
            @RequestParam(required = false) final String name,
            @RequestParam(required = false) final String description,
            @RequestParam(required = false) final String dateSort,
            @RequestParam(required = false) final String nameSort,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final List<GiftCertificateDto> dtoGiftCertificates
                = giftCertificateService.readAll(tags, name, description, dateSort, nameSort, page, size);
        dtoGiftCertificates.forEach(giftCertificateDto -> {
            hateoasCreator.linkGiftCertificateDtoOne(giftCertificateDto);
            giftCertificateDto.getDtoTags().forEach(hateoasCreator::linkTagDto);
        });

        return new ResponseEntity<>(dtoGiftCertificates, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<GiftCertificateDto> readOne(@PathVariable final int id) {
        final GiftCertificateDto giftCertificateDto = giftCertificateService.readOne(id);
        hateoasCreator.linkGiftCertificateDtoOne(giftCertificateDto);
        giftCertificateDto.getDtoTags().forEach(hateoasCreator::linkTagDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<GiftCertificateDto> update(@PathVariable final Integer id,
                                                 @RequestBody final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.setId(id);
        final GiftCertificateDto updatedGiftCertificateDto = giftCertificateService.update(giftCertificateDto);
        hateoasCreator.linkGiftCertificateDtoOne(giftCertificateDto);
        updatedGiftCertificateDto.getDtoTags().forEach(hateoasCreator::linkTagDto);
        return new ResponseEntity<>(updatedGiftCertificateDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<GiftCertificateDto> deleteById(@PathVariable int id) {
        final GiftCertificateDto giftCertificateDto = giftCertificateService.deleteById(id);
        hateoasCreator.linkGiftCertificateDtoOne(giftCertificateDto);
        giftCertificateDto.getDtoTags().forEach(hateoasCreator::linkTagDto);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }
}
