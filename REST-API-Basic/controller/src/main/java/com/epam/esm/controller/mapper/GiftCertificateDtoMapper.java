package com.epam.esm.controller.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.controller.dto.GiftCertificateDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
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
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);

    }

    @Override
    public GiftCertificate dtoToModel(final GiftCertificateDto giftCertificateDto) {
        final GiftCertificate giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificate.class);
        emptyTagsIfNull(giftCertificate);
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
            giftCertificate.setTags(new HashSet<>());
        }
    }
}
