package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dtomapper.DtoMapper;
import com.epam.esm.service.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private DtoMapper<Tag, TagDto> mapper;
    @Mock
    private TagDao tagDao;
    @Mock
    private GiftCertificateTagDao giftCertificateTagDao;
    @Mock
    private TagValidator tagValidator;
    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag1;
    private Tag tag2;
    private TagDto tagDto1;
    private TagDto tagDto2;

    @BeforeEach
    public void setUp() {
        tag1 = new Tag(1, "tag1");
        tag2 = new Tag(2, "tag2");
        tagDto1 = new TagDto(1, "tag1");
        tagDto2 = new TagDto(2, "tag2");
    }

    @Test
    void shouldCreateTag_On_Create() {
        //Given
        when(tagDao.readOneByName(tag1.getName())).thenReturn(Optional.empty());
        when(tagDao.create(tag1)).thenReturn(tag1);
        when(mapper.dtoToModel(tagDto1)).thenReturn(tag1);
        when(mapper.modelToDto(tag1)).thenReturn(tagDto1);
        //When
        final TagDto actualTag = tagService.create(tagDto1);
        //Then
        assertEquals(tagDto1, actualTag);
        verify(tagValidator, only()).createValidate(tagDto1);
        verify(tagDao, times(1)).readOneByName(tag1.getName());
        verify(tagDao, times(1)).create(tag1);
        verifyNoMoreInteractions(tagDao);
        verify(mapper, times(1)).dtoToModel(tagDto1);
        verify(mapper, times(1)).modelToDto(tag1);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowException_On_Create() {
        //Given
        when(tagDao.readOneByName(tag1.getName())).thenReturn(Optional.of(tag1));
        when(mapper.dtoToModel(tagDto1)).thenReturn(tag1);
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.create(tagDto1));
        //Then
        assertEquals("Tag with name = " + tag1.getName() + " is already exist", serviceException.getMessage());
        verify(tagValidator, only()).createValidate(tagDto1);
        verify(tagDao, only()).readOneByName(tag1.getName());
        verify(mapper, only()).dtoToModel(tagDto1);
    }

    @Test
    void shouldReturnTag_On_ReadOne() {
        //Given
        when(tagDao.readOne(tag1.getId())).thenReturn(Optional.of(tag1));
        when(mapper.modelToDto(tag1)).thenReturn(tagDto1);
        //When
        final TagDto actualTag = tagService.readOne(tag1.getId());
        //Then
        assertEquals(tagDto1, actualTag);
        verify(tagDao, only()).readOne(tag1.getId());
        verify(mapper, only()).modelToDto(tag1);
    }

    @Test
    void shouldThrowException_On_ReadOne_NotFound() {
        //Given
        when(tagDao.readOne(tag1.getId())).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.readOne(tag1.getId()));
        //Then
        assertEquals("Tag with id = " + tag1.getId() + " not found",
                serviceException.getMessage());
        verify(tagDao, only()).readOne(tag1.getId());
    }

    @Test
    void shouldReturnTags_On_ReadAll() {
        //Given
        final List<Tag> tags = Arrays.asList(tag1, tag2);
        final List<TagDto> expectedTags = Arrays.asList(tagDto1, tagDto2);
        //When
        when(tagDao.readAll()).thenReturn(tags);
        when(mapper.modelsToDto(tags)).thenReturn(expectedTags);
        final List<TagDto> actualTags = tagService.readAll();
        //Then
        assertEquals(expectedTags, actualTags);
        verify(tagDao, only()).readAll();
        verify(mapper, only()).modelsToDto(tags);
    }

    @Test
    void shouldDeleteTag_On_DeleteById() {
        //Given
        when(tagDao.readOne(tag1.getId())).thenReturn(Optional.of(tag1));
        //When
        tagService.deleteById(tag1.getId());
        //Then
        verify(tagDao, times(1)).readOne(tag1.getId());
        verify(tagDao, times(1)).deleteById(tag1.getId());
        verifyNoMoreInteractions(tagDao);
        verify(giftCertificateTagDao, only()).deleteGiftCertificateTagByTagId(tag1.getId());
    }
}