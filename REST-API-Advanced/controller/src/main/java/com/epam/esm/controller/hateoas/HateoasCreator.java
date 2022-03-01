package com.epam.esm.controller.hateoas;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HateoasCreator {

    public void linkGiftCertificateDtoOne(final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .create(giftCertificateDto)).withRel("method").withType("POST"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .update(giftCertificateDto.getId(), giftCertificateDto)).withRel("method").withType("PATCH"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .deleteById(giftCertificateDto.getId())).withRel("method").withType("DELETE"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .readOne(giftCertificateDto.getId()))
                        .withSelfRel());
    }

    public void linkGiftCertificateDto(final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .readOne(giftCertificateDto.getId()))
                        .withSelfRel());
    }

    public void linkTagDto(final TagDto tagDto) {
        tagDto
                .add(linkTo(methodOn(TagController.class)
                        .readOne(tagDto.getId(), null))
                        .withSelfRel().expand());
    }

    public void linkTagDtoOne(final TagDto tagDto) {
        tagDto
                .add(linkTo(methodOn(TagController.class)
                        .create(tagDto)).withRel("method").withType("POST"))
                .add(linkTo(methodOn(TagController.class)
                        .deleteById(tagDto.getId())).withRel("method").withType("DELETE"))
                .add(linkTo(methodOn(TagController.class)
                        .readOne(tagDto.getId(), null))
                        .withSelfRel().expand());
    }

    public void linkUserDto(final UserDto userDto) {
        userDto.add(linkTo(methodOn(UserController.class)
                .readOne(userDto.getId()))
                .withSelfRel());
    }

    public void linkUserDtoOne(final UserDto userDto) {
        userDto
                .add(linkTo(methodOn(UserController.class)
                        .readOne(userDto.getId()))
                        .withSelfRel());
    }

    public void linkOrderDtoOne(final OrderDto orderDto) {
        orderDto
                .add(linkTo(methodOn(OrderController.class)
                        .create(orderDto)).withRel("method").withType("POST"))
                .add(linkTo(methodOn(OrderController.class)
                        .readOne(orderDto.getId()))
                        .withSelfRel());
    }

    public void linkOrderDto(final OrderDto orderDto) {
        orderDto
                .add(linkTo(methodOn(OrderController.class)
                        .readOne(orderDto.getId()))
                        .withSelfRel());
    }
}
