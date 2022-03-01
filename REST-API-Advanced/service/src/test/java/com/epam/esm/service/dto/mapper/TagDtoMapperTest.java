package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagDtoMapperTest {

    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private TagDtoMapper tagDtoMapper;

    private Tag tag;
    private TagDto tagDto;

    @BeforeEach
    public void setUp() {
        tag = new Tag();
        tagDto = new TagDto();
    }

    @Test
    void shouldMapTagDto_For_Tag() {
        //Given
        when(mapper.map(tag, TagDto.class)).thenReturn(tagDto);
        //When
        final TagDto actualTagDto = tagDtoMapper.modelToDto(tag);
        //Then
        assertEquals(tagDto, actualTagDto);
        verify(mapper, only()).map(tag, TagDto.class);
    }

    @Test
    void shouldMapTag_For_TagDto() {
        //Given
        when(mapper.map(tagDto, Tag.class)).thenReturn(tag);
        //When
        final Tag actualTag = tagDtoMapper.dtoToModel(tagDto);
        //Then
        assertEquals(tag, actualTag);
        verify(mapper, only()).map(tagDto, Tag.class);
    }

    @Test
    void shouldMapDtoTags_For_Tags() {
        //Given
        final List<Tag> tags = Collections.singletonList(tag);
        when(mapper.map(tag, TagDto.class)).thenReturn(tagDto);
        //When
        final List<TagDto> actualDtoTags = tagDtoMapper.modelsToDto(tags);
        //Then
        assertEquals(Collections.singletonList(tagDto), actualDtoTags);
        verify(mapper, only()).map(tag, TagDto.class);
    }

    @Test
    void shouldMapTags_For_DtoTags() {
        //Given
        final List<TagDto> dtoTags = Collections.singletonList(tagDto);
        when(mapper.map(tagDto, Tag.class)).thenReturn(tag);
        //When
        final List<Tag> actualTags = tagDtoMapper.dtoToModels(dtoTags);
        //Then
        assertEquals(Collections.singletonList(tag), actualTags);
        verify(mapper, only()).map(tagDto, Tag.class);
    }
}