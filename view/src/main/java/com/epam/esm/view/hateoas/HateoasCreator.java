package com.epam.esm.view.hateoas;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.view.controller.GiftCertificateController;
import com.epam.esm.view.controller.OrderController;
import com.epam.esm.view.controller.TagController;
import com.epam.esm.view.controller.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class HateoasCreator {

    private final PagedResourcesAssembler<TagDto> pagedResourcesAssemblerTag;
    private final PagedResourcesAssembler<UserDto> pagedResourcesAssemblerUser;
    private final PagedResourcesAssembler<GiftCertificateDto> pagedResourcesAssemblerGift;
    private final PagedResourcesAssembler<OrderDto> pagedResourcesAssemblerOrder;

    public GiftCertificateDto linkGiftCertificateDtoOne(final GiftCertificateDto giftCertificateDto) {
        return giftCertificateDto
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

    public PagedModel<GiftCertificateDto> linkGiftCertificateDtos(Page<GiftCertificateDto> giftCertificateDtos) {
        return pagedResourcesAssemblerGift.toModel(giftCertificateDtos, this::linkGiftCertificateDtoOne);
    }

    public TagDto linkTagDto(final TagDto tagDto) {
        return tagDto
                .add(linkTo(methodOn(TagController.class)
                        .readOne(tagDto.getId()))
                        .withSelfRel().expand());
    }

    public TagDto linkTagDtoOne(final TagDto tagDto) {
        return tagDto
                .add(linkTo(methodOn(TagController.class)
                        .create(tagDto)).withRel("method").withType("POST"))
                .add(linkTo(methodOn(TagController.class)
                        .deleteById(tagDto.getId())).withRel("method").withType("DELETE"))
                .add(linkTo(methodOn(TagController.class)
                        .readOne(tagDto.getId()))
                        .withSelfRel().expand());
    }

    public PagedModel<TagDto> linkTagDtos(Page<TagDto> dtoTags) {
        return pagedResourcesAssemblerTag.toModel(dtoTags, this::linkTagDtoOne);
    }

    public UserDto linkUserDto(final UserDto userDto) {
        return userDto
                .add(linkTo(methodOn(UserController.class)
                        .readOne(userDto.getId()))
                        .withSelfRel());
    }

    public PagedModel<UserDto> linkUserDtos(Page<UserDto> dtoUsers) {
        return pagedResourcesAssemblerUser.toModel(dtoUsers, this::linkUserDto);
    }

    public PagedModel<OrderDto> linkOrderDtos(Page<OrderDto> dtoOrders) {
        return pagedResourcesAssemblerOrder.toModel(dtoOrders, this::linkOrderDtoOne);
    }

    public OrderDto linkOrderDtoOne(final OrderDto orderDto) {
        return orderDto
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
