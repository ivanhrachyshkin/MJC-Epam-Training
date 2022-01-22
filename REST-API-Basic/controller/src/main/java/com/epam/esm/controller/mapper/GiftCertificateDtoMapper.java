package com.epam.esm.controller.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.GiftCertificateDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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
        final GiftCertificateDto giftCertificateDto = modelMapper.map(giftCertificate, GiftCertificateDto.class);
        return giftCertificateDto;

    }

    @Override
    public GiftCertificate dtoToModel(final GiftCertificateDto giftCertificateDto) {
        final GiftCertificate giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificate.class);
        return giftCertificate;
    }

    @Override
    public List<GiftCertificateDto> modelsToDto(List<GiftCertificate> giftCertificates) {
        return giftCertificates
                .stream()
                .map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public List<GiftCertificate> dtoToModels(List<GiftCertificateDto> giftCertificateDtos) {
        return giftCertificateDtos
                .stream()
                .map(giftCertificateDto -> modelMapper.map(giftCertificateDto, GiftCertificate.class))
                .collect(Collectors.toList());
    }
}
