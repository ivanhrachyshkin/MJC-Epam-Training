//package com.epam.esm.controller;
//
//
//import com.epam.esm.service.GiftCertificateService;
//import com.epam.esm.service.dto.GiftCertificateDto;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * The intention of controller - handling of the /gift resource.
// */
//@RestController
//@RequestMapping(value = "/gift")
//public class GiftCertificateController {
//
//    private final GiftCertificateService giftCertificateService;
//
//    public GiftCertificateController(final GiftCertificateService giftCertificateService) {
//        this.giftCertificateService = giftCertificateService;
//    }
//
//    /**
//     * The intention of mapping - handling of the creation operation.
//     *
//     * @param giftCertificateDto the request body representation of the gift certificate.
//     * @return the response body representation of the gift certificate.
//     */
//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.CREATED)
//    public GiftCertificateDto create(@RequestBody GiftCertificateDto giftCertificateDto) {
//        return giftCertificateService.create(giftCertificateDto);
//    }
//
//    /**
//     * The intention of mapping - handling of the read operation.
//     *
//     * @param tag the request param representation of the tag's name.
//     * @param name the request param representation of the gift certificate's name.
//     * @param description the request param representation of the gift certificate's description.
//     * @param dateSortDirection the request param representation of sort direction by date.
//     * @param nameSortDirection the request param representation of sort direction by name.
//     * @return the response body representation of the gift certificates.
//     */
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public List<GiftCertificateDto> readAll(@RequestParam(required = false) final String tag,
//                                            @RequestParam(required = false) final String name,
//                                            @RequestParam(required = false) final String description,
//                                            @RequestParam(required = false) final Boolean dateSortDirection,
//                                            @RequestParam(required = false) final Boolean nameSortDirection) {
//        return giftCertificateService.readAll(tag, name, description, dateSortDirection, nameSortDirection);
//    }
//
//    /**
//     * The intention of mapping - handling of the read one operation.
//     *
//     * @param id the request path variable representation of the gift certificate's id.
//     * @return the response body representation of the gift certificate.
//     */
//    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public GiftCertificateDto readOne(@PathVariable final int id) {
//        return giftCertificateService.readOne(id);
//    }
//
//    /**
//     * The intention of mapping - handling of the update operation.
//     *
//     * @param id the request path variable representation of the gift certificate's id.
//     * @param giftCertificateDto the request body representation of the gift certificate.
//     * @return the response body representation of the gift certificate.
//     */
//    @PatchMapping(value = "/{id}",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public GiftCertificateDto update(@PathVariable final int id, @RequestBody final GiftCertificateDto giftCertificateDto) {
//        giftCertificateDto.setId(id);
//        return giftCertificateService.update(giftCertificateDto);
//    }
//
//    /**
//     * The intention of mapping - handling of the update operation.
//     * @param id the request path variable representation of the gift certificate's id.
//     */
//    @DeleteMapping(value = "/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteById(@PathVariable int id) {
//        giftCertificateService.deleteById(id);
//    }
//}
