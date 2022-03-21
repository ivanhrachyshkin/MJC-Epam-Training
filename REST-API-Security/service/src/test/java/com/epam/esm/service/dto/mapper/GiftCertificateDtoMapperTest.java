package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateDtoMapperTest {

    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private GiftCertificateDtoMapper giftCertificateDtoMapper;

    @Mock
    private GiftCertificate giftCertificate;
    @Mock
    private GiftCertificateDto dtoGiftCertificate;
    @Mock
    private Tag tag;
    @Mock
    private TagDto dtoTag;

    @Test
    void shouldMapGiftCertificateDto_For_GiftCertificateWithTags() {
        //Given
        when(giftCertificate.getTags()).thenReturn(Collections.singleton(tag));
        when(mapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(dtoGiftCertificate);
        //When
        final GiftCertificateDto actualGiftCertificateDto = giftCertificateDtoMapper.modelToDto(giftCertificate);
        //Then
        assertEquals(dtoGiftCertificate, actualGiftCertificateDto);
        verify(mapper, only()).map(giftCertificate, GiftCertificateDto.class);
    }

    @Test
    void shouldMapGiftCertificateDto_For_GiftCertificateWithNullTags() {
        //Given
        when(giftCertificate.getTags()).thenReturn(null);
        when(mapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(dtoGiftCertificate);
        //When
        final GiftCertificateDto actualGiftCertificateDto = giftCertificateDtoMapper.modelToDto(giftCertificate);
        //Then
        assertEquals(dtoGiftCertificate, actualGiftCertificateDto);
        verify(mapper, only()).map(giftCertificate, GiftCertificateDto.class);
    }

    @Test
    void shouldMapGiftCertificate_For_GiftCertificateDtoWithTags() {
        //Given
        when(dtoGiftCertificate.getDtoTags()).thenReturn(Collections.singleton(dtoTag));
        when(mapper.map(dtoGiftCertificate, GiftCertificate.class)).thenReturn(giftCertificate);
        when(mapper.map(dtoTag, Tag.class)).thenReturn(tag);
        //When
        final GiftCertificate actualGiftCertificate = giftCertificateDtoMapper.dtoToModel(dtoGiftCertificate);
        //Then
        assertEquals(giftCertificate, actualGiftCertificate);
        verify(mapper, times(1)).map(dtoGiftCertificate, GiftCertificate.class);
        verify(mapper, times(1)).map(dtoTag, Tag.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldMapGiftCertificate_For_GiftCertificateDtoWithNullTags() {
        //Given
        when(dtoGiftCertificate.getDtoTags()).thenReturn(null);
        when(mapper.map(dtoGiftCertificate, GiftCertificate.class)).thenReturn(giftCertificate);
        //When
        final GiftCertificate actualGiftCertificate = giftCertificateDtoMapper.dtoToModel(dtoGiftCertificate);
        //Then
        assertEquals(giftCertificate, actualGiftCertificate);
        verify(mapper, only()).map(dtoGiftCertificate, GiftCertificate.class);
    }

    @Test
    void shouldMapDtoGiftCertificates_For_GiftCertificates() {
        //Given
        when(giftCertificate.getTags()).thenReturn(Collections.singleton(tag));
        final Page<GiftCertificate> giftCertificates
                = new PageImpl<>(Collections.singletonList(giftCertificate));
        final Page<GiftCertificateDto> expectedDtoGiftCertificates
                = new PageImpl<>(Collections.singletonList(dtoGiftCertificate));

        when(mapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(dtoGiftCertificate);
        //When
        final Page<GiftCertificateDto> actualDtoGiftCertificates
                = giftCertificateDtoMapper.modelsToDto(giftCertificates);
        //Then
        assertEquals(expectedDtoGiftCertificates, actualDtoGiftCertificates);
        assertEquals(expectedDtoGiftCertificates.getTotalElements(), actualDtoGiftCertificates.getTotalElements());
        assertEquals(expectedDtoGiftCertificates.getTotalPages(), actualDtoGiftCertificates.getTotalPages());
        verify(mapper, only()).map(giftCertificate, GiftCertificateDto.class);
    }
}
