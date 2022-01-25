package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import com.epam.esm.service.validator.CreateTagValidator;
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
    private TagDao tagDao;
    @Mock
    private GiftCertificateTagDao giftCertificateTagDao;
    @Mock
    private CreateTagValidator createTagValidator;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void shouldCreateTag_On_Create() {
        //Given
        final Tag creatingTag = new Tag("tag1");
        final Tag expectedTag = new Tag(1, "tag1");
        when(tagDao.readOneByName(creatingTag.getName())).thenReturn(Optional.empty());
        when(tagDao.create(creatingTag)).thenReturn(expectedTag);
        //When
        final Tag actualTag = tagService.create(creatingTag);
        //Then
        assertEquals(expectedTag, actualTag);
        verify(createTagValidator, only()).validate(creatingTag);
        verify(tagDao, times(1)).readOneByName(creatingTag.getName());
        verify(tagDao, times(1)).create(creatingTag);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    void shouldThrowException_On_Create() {
        //Given
        final Tag tag = new Tag(1, "tag1");
        when(tagDao.readOneByName(tag.getName())).thenReturn(Optional.of(tag));
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.create(tag));
        //Then
        assertEquals("Tag with name = " + tag.getName() + " is already exist", serviceException.getMessage());
        verify(createTagValidator, only()).validate(tag);
        verify(tagDao, only()).readOneByName(tag.getName());
    }

    @Test
    void shouldReturnTag_On_ReadOne() {
        //Given
        final Tag expectedTag = new Tag(1, "tag1");
        //When
        when(tagDao.readOne(expectedTag.getId())).thenReturn(Optional.of(expectedTag));
        final Tag actualTag = tagService.readOne(expectedTag.getId());
        //Then
        assertEquals(expectedTag, actualTag);
        verify(tagDao, only()).readOne(expectedTag.getId());
    }

    @Test
    void shouldThrowException_On_ReadOne_NotFound() {
        //Given
        final Tag tag = new Tag(1, "tag1");
        //When
        when(tagDao.readOne(tag.getId())).thenReturn(Optional.empty());
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.readOne(tag.getId()));
        //Then
        assertEquals("Tag with id = " + tag.getId() + " not found",
                serviceException.getMessage());
        verify(tagDao, only()).readOne(tag.getId());
    }

    @Test
    void shouldReturnTags_On_ReadAll() {
        //Given
        final Tag tag1 = new Tag(1, "tag1");
        final Tag tag2 = new Tag(2, "tag2");
        final List<Tag> expectedTags = Arrays.asList(tag1, tag2);
        //When
        when(tagDao.readAll()).thenReturn(expectedTags);
        final List<Tag> actualTags = tagService.readAll();
        //Then
        assertEquals(expectedTags, actualTags);
        verify(tagDao, only()).readAll();
    }

    @Test
    void shouldDeleteTag_On_DeleteById() {
        //Given
        final Tag tag = new Tag(1, "tag1");
        when(tagDao.readOne(tag.getId())).thenReturn(Optional.of(tag));
        //When
        tagService.deleteById(tag.getId());
        //Then
        verify(tagDao, times(1)).readOne(tag.getId());
        verify(tagDao, times(1)).deleteById(tag.getId());
        verifyNoMoreInteractions(tagDao);
        verify(giftCertificateTagDao, only()).deleteGiftCertificateTagByTagId(tag.getId());
    }
}