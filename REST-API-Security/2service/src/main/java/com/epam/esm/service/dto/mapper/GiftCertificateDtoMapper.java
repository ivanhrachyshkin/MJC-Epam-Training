package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GiftCertificateDtoMapper implements DtoMapper<GiftCertificate, GiftCertificateDto> {

    private final ModelMapper modelMapper;

    @Override
    public GiftCertificateDto modelToDto(final GiftCertificate giftCertificate) {
        final GiftCertificateDto dtoGiftCertificate = modelMapper.map(giftCertificate, GiftCertificateDto.class);
        emptyTagsIfNull(giftCertificate);
        final Set<TagDto> dtoTags = giftCertificate
                .getTags()
                .stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName(), tag.getActive()))
                .collect(Collectors.toSet());
        dtoGiftCertificate.setDtoTags(dtoTags);
        return dtoGiftCertificate;
    }

    @Override
    public GiftCertificate dtoToModel(final GiftCertificateDto giftCertificateDto) {
        final GiftCertificate giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificate.class);
        emptyTagsIfNull(giftCertificate);
        final Set<Tag> tags = giftCertificate
                .getTags()
                .stream()
                .map(tagDto -> modelMapper.map(tagDto, Tag.class))
                .collect(Collectors.toSet());
        giftCertificate.setTags(tags);
        return giftCertificate;
    }

    @Override
    public Page<GiftCertificateDto> modelsToDto(Page<GiftCertificate> giftCertificates) {
        List<GiftCertificateDto> collect = giftCertificates
                .stream()
                .map(this::modelToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(collect, giftCertificates.getPageable(), giftCertificates.getTotalElements());
    }

    @Override
    public List<GiftCertificate> dtoToModels(List<GiftCertificateDto> dtoGiftCertificates) {
        return dtoGiftCertificates
                .stream()
                .map(this::dtoToModel)
                .collect(Collectors.toList());
    }

    private void emptyTagsIfNull(final GiftCertificate giftCertificate) {
        if (giftCertificate.getTags() == null) {
            giftCertificate.setTags(Collections.emptySet());
        }
    }
}
