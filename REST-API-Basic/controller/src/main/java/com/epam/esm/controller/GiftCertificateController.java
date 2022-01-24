package com.epam.esm.controller;

import com.epam.esm.controller.mapper.DtoMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.controller.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/gift")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final DtoMapper<GiftCertificate, GiftCertificateDto> mapper;

    public GiftCertificateController(GiftCertificateService giftCertificateService, DtoMapper<GiftCertificate, GiftCertificateDto> mapper) {
        this.giftCertificateService = giftCertificateService;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody GiftCertificateDto giftCertificateDto) {

        final GiftCertificate giftCertificate = mapper.dtoToModel(giftCertificateDto);
        final GiftCertificate createdGiftCertificate = giftCertificateService.create(giftCertificate);
        return mapper.modelToDto(createdGiftCertificate);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> readAll(@RequestParam(required = false) final String tag,
                                            @RequestParam(required = false) final String name,
                                            @RequestParam(required = false) final String description,
                                            @RequestParam(required = false) final Boolean asc) {
        final List<GiftCertificate> giftCertificates = giftCertificateService.readAll(tag, name, description, asc);
        return mapper.modelsToDto(giftCertificates);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto readOne(@PathVariable final int id) {
        final GiftCertificate giftCertificate = giftCertificateService.readOne(id);
        return mapper.modelToDto(giftCertificate);
    }

    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GiftCertificateDto update(@PathVariable final int id, @RequestBody final GiftCertificateDto giftCertificateDto) {
        final GiftCertificate giftCertificate = mapper.dtoToModel(giftCertificateDto);
        giftCertificate.setId(id);
        final GiftCertificate updatedGiftCertificate = giftCertificateService.update(giftCertificate);
        return mapper.modelToDto(updatedGiftCertificate);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int id) {
        giftCertificateService.deleteById(id);
    }
}
