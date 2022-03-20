package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagDtoMapperTest {

    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private TagDtoMapper tagDtoMapper;

    @Mock
    private Tag tag;
    @Mock
    private Tag tag2;
    @Mock
    private TagDto dtoTag;
    @Mock
    private TagDto dtoTag2;


    @Test
    void shouldMapTagDto_For_Tag() {
        //Given
        when(mapper.map(tag, TagDto.class)).thenReturn(dtoTag);
        //When
        final TagDto actualTagDto = tagDtoMapper.modelToDto(tag);
        //Then
        assertEquals(dtoTag, actualTagDto);
        verify(mapper, only()).map(tag, TagDto.class);
    }

    @Test
    void shouldMapTag_For_TagDto() {
        //Given
        when(mapper.map(dtoTag, Tag.class)).thenReturn(tag);
        //When
        final Tag actualTag = tagDtoMapper.dtoToModel(dtoTag);
        //Then
        assertEquals(tag, actualTag);
        verify(mapper, only()).map(dtoTag, Tag.class);
    }

    @Test
    void shouldMapDtoTags_For_Tags() {
        //Given
        final Page<Tag> tags = new PageImpl<>(Arrays.asList(tag, tag2));
        final Page<TagDto> expectedDtoTags = new PageImpl<>(Arrays.asList(dtoTag, dtoTag2));
        when(mapper.map(tag, TagDto.class)).thenReturn(dtoTag);
        when(mapper.map(tag2, TagDto.class)).thenReturn(dtoTag2);
        //When
        final Page<TagDto> actualDtoTags = tagDtoMapper.modelsToDto(tags);
        //Then
        assertEquals(expectedDtoTags, actualDtoTags);
        assertEquals(expectedDtoTags.getTotalElements(), actualDtoTags.getTotalElements());
        assertEquals(expectedDtoTags.getTotalPages(), actualDtoTags.getTotalPages());
        verify(mapper, times(1)).map(tag, TagDto.class);
        verify(mapper, times(1)).map(tag2, TagDto.class);
        verifyNoMoreInteractions(mapper);

    }
}