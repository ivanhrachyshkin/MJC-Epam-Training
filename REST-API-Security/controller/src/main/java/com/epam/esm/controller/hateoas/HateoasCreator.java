package com.epam.esm.controller.hateoas;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
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
        return pagedResourcesAssemblerGift.toModel(giftCertificateDtos,
                this::linkGiftCertificateDtoOne, linkTo(methodOn(GiftCertificateController.class)
                .readAll(null, null, Pageable.unpaged()))
                .withSelfRel());
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
        return pagedResourcesAssemblerTag.toModel(dtoTags, this::linkTagDtoOne, linkTo(methodOn(TagController.class)
                .readAll(true, Pageable.unpaged()))
                .withSelfRel());
    }

    public UserDto linkUserDto(final UserDto userDto) {
        return userDto
                .add(linkTo(methodOn(UserController.class)
                        .readOne(userDto.getId()))
                        .withSelfRel());
    }

    public PagedModel<UserDto> linkUserDtos(Page<UserDto> dtoUsers) {
        return pagedResourcesAssemblerUser.toModel(dtoUsers, this::linkUserDto, linkTo(methodOn(UserController.class)
                .readAll(Pageable.unpaged()))
                .withSelfRel());
    }

    public PagedModel<OrderDto> linkOrderDtos(Page<OrderDto> dtoOrders) {
        return pagedResourcesAssemblerOrder.toModel(dtoOrders,
                this::linkOrderDtoOne, linkTo(methodOn(OrderController.class)
                .readAll(Pageable.unpaged()))
                .withSelfRel());
    }

    public OrderDto linkOrderDtoOne(final OrderDto orderDto) {
      return  orderDto
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
