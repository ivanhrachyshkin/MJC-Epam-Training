package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GiftCertificateMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGiftCertificateFromRaw(GiftCertificate raw, @MappingTarget GiftCertificate entity);
}
