package com.epam.esm.service.dtomapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GiftCertificateDtoMapper implements DtoMapper<GiftCertificate, GiftCertificateDto> {

    private final ModelMapper modelMapper;

    public GiftCertificateDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public GiftCertificateDto modelToDto(final GiftCertificate giftCertificate) {
        emptyTagsIfNull(giftCertificate);
        final Set<TagDto> dtoTags = giftCertificate
                .getTags()
                .stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toSet());
        final GiftCertificateDto dtoGiftCertificate = modelMapper.map(giftCertificate, GiftCertificateDto.class);
        dtoGiftCertificate.setTags(dtoTags);
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
    public List<GiftCertificateDto> modelsToDto(List<GiftCertificate> giftCertificates) {
        return giftCertificates
                .stream()
                .map(this::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificate> dtoToModels(List<GiftCertificateDto> giftCertificateDtos) {
        return giftCertificateDtos
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
