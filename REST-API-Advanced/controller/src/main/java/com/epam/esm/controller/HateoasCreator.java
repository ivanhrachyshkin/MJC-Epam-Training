package com.epam.esm.controller;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HateoasCreator {

    public EntityModel<GiftCertificateDto> linkGiftCertificateDtoWithTagsHal(
            final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto
                .getDtoTags()
                .forEach(this::linkTagDto);

        return EntityModel.of(giftCertificateDto,
                linkTo(methodOn(GiftCertificateController.class).readOne(giftCertificateDto.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(GiftCertificateController.class)
                                .create(null)))
                        .andAffordance(afford(methodOn(GiftCertificateController.class)
                                .update(null, null)))
                        .andAffordance(afford(methodOn(GiftCertificateController.class)
                                .deleteById(giftCertificateDto.getId()))));
    }

    public void linkGiftCertificateDtoWithTags(final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .readOne(giftCertificateDto.getId()))
                .withSelfRel());

        giftCertificateDto
                .getDtoTags()
                .forEach(this::linkTagDto);
    }

    public void linkGiftCertificateDto(final GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .readOne(giftCertificateDto.getId()))
                .withSelfRel());
    }

    public EntityModel<TagDto> linkTagDtoHal(final TagDto tagDto) {
        return EntityModel.of(tagDto,
                linkTo(methodOn(TagController.class).readOne(tagDto.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(TagController.class).create(null)))
                        .andAffordance(afford(methodOn(TagController.class).deleteById(tagDto.getId()))));
    }

    public void linkTagDto(final TagDto tagDto) {
        tagDto
                .add(linkTo(methodOn(TagController.class)
                        .readAll())
                        .slash(tagDto.getId())
                        .withSelfRel());
    }

    public void linkUserDtoWithOrders(final UserDto userDto) {
        userDto.add(linkTo(methodOn(UserController.class)
                .readOne(userDto.getId()))
                .withSelfRel());

        userDto
                .getDtoOrders()
                .forEach(this::linkOrderDto);
    }

    public EntityModel<UserDto> linkUserDtoHal(final UserDto userDto) {
        userDto
                .getDtoOrders()
                .forEach(this::linkOrderDto);

        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).readOne(userDto.getId())).withSelfRel());
    }

    public void linkUserDto(final UserDto userDto) {
        userDto.add(linkTo(methodOn(UserController.class)
                .readOne(userDto.getId()))
                .withSelfRel());
    }

    public void linkOrderDtoWithUserAndGiftCertificate(final OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(OrderController.class)
                .readOne(orderDto.getId()))
                .withSelfRel());

        linkUserDto(orderDto.getUserDto());
        linkGiftCertificateDto(orderDto.getGiftCertificateDto());
    }

    public EntityModel<OrderDto> linkOrderDtoWithUserAndGiftCertificateHal(final OrderDto orderDto) {
        linkUserDto(orderDto.getUserDto());
        linkGiftCertificateDto(orderDto.getGiftCertificateDto());

        return EntityModel.of(orderDto,
                linkTo(methodOn(OrderController.class).readOne(orderDto.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(OrderController.class).create(null))));
    }

    public void linkOrderDto(final OrderDto orderDto) {
        orderDto
                .add(linkTo(methodOn(OrderController.class)
                        .readOne(orderDto.getId()))
                        .withSelfRel());
    }
}
