package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateDtoMapperTest {

    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private GiftCertificateDtoMapper giftCertificateDtoMapper;

    private Tag tag;
    private TagDto tagDto;
    private GiftCertificate giftCertificate;
    private GiftCertificateDto giftCertificateDto;

    @BeforeEach
    public void setUp() {
        tag = new Tag(1);
        tagDto = new TagDto(1);

        giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        giftCertificateDto = new GiftCertificateDto(1);
        giftCertificate.setTags(new HashSet<>(Collections.singletonList(tag)));
        giftCertificateDto.setDtoTags(new HashSet<>(Collections.singletonList(tagDto)));
    }

    @Test
    void shouldMapGiftCertificateDto_For_GiftCertificate() {
        //Given
        when(mapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(giftCertificateDto);
        //When
        final GiftCertificateDto actualGiftCertificateDto = giftCertificateDtoMapper.modelToDto(giftCertificate);
        //Then
        assertEquals(giftCertificateDto, actualGiftCertificateDto);

        verify(mapper, only()).map(giftCertificate, GiftCertificateDto.class);
    }

    @Test
    void shouldMapGiftCertificate_For_GiftCertificateDto() {
        //Given
        when(mapper.map(giftCertificateDto, GiftCertificate.class)).thenReturn(giftCertificate);
        when(mapper.map(tagDto, Tag.class)).thenReturn(tag);
        //When
        final GiftCertificate actualGiftCertificate = giftCertificateDtoMapper.dtoToModel(giftCertificateDto);
        //Then
        assertEquals(giftCertificate, actualGiftCertificate);

        verify(mapper, times(1)).map(giftCertificateDto, GiftCertificate.class);
        verify(mapper, times(1)).map(tagDto, Tag.class);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldMapGiftCertificateDtos_For_GiftCertificates() {
        //Given
        final List<GiftCertificate> giftCertificates = Collections.singletonList(giftCertificate);
        when(mapper.map(giftCertificate, GiftCertificateDto.class)).thenReturn(giftCertificateDto);
        //When
        final List<GiftCertificateDto> actualGiftCertificateDtos = giftCertificateDtoMapper.modelsToDto(giftCertificates);
        //Then
        assertEquals(Collections.singletonList(giftCertificateDto), actualGiftCertificateDtos);

        verify(mapper, only()).map(giftCertificate, GiftCertificateDto.class);
    }

    @Test
    void shouldMapGiftCertificates_For_GiftCertificateDtos() {
        //Given
        final List<GiftCertificateDto> giftCertificateDtos = Collections.singletonList(giftCertificateDto);
        when(mapper.map(giftCertificateDto, GiftCertificate.class)).thenReturn(giftCertificate);
        //When
        final List<GiftCertificate> actualGiftCertificates = giftCertificateDtoMapper.dtoToModels(giftCertificateDtos);
        //Then
        assertEquals(Collections.singletonList(giftCertificate), actualGiftCertificates);

        verify(mapper, times(1)).map(giftCertificateDto, GiftCertificate.class);
        verify(mapper, times(1)).map(tagDto, Tag.class);
        verifyNoMoreInteractions(mapper);
    }
}