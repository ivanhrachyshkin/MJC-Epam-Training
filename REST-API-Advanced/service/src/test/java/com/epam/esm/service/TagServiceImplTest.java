package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    private final DummyRb dummyRb = new DummyRb();
    @Mock
    private DtoMapper<Tag, TagDto> mapper;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagValidator tagValidator;
    @Mock
    private PaginationValidator paginationValidator;
    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag1;
    private Tag tag2;
    private TagDto tagDto1;
    private TagDto tagDto2;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(tagService, "rb", dummyRb);
        tag1 = new Tag(1);
        tag2 = new Tag(2);
        tagDto1 = new TagDto(1);
        tagDto2 = new TagDto(2);
    }

    @Test
    void shouldCreateTag_On_Create() {
        //Given
        when(tagRepository.readOneByName(tag1.getName())).thenReturn(Optional.empty());
        when(tagRepository.create(tag1)).thenReturn(tag1);
        when(mapper.dtoToModel(tagDto1)).thenReturn(tag1);
        when(mapper.modelToDto(tag1)).thenReturn(tagDto1);
        //When
        final TagDto actualTag = tagService.create(tagDto1);
        //Then
        assertEquals(tagDto1, actualTag);
        verify(tagValidator, only()).createValidate(tagDto1);
        verify(tagRepository, times(1)).readOneByName(tag1.getName());
        verify(tagRepository, times(1)).create(tag1);
        verifyNoMoreInteractions(tagRepository);
        verify(mapper, times(1)).dtoToModel(tagDto1);
        verify(mapper, times(1)).modelToDto(tag1);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowException_On_Create() {
        //Given
        dummyRb.setMessage("tag.alreadyExists.name", "Tag with name = %s is already exist");
        final String message = String.format("Tag with name = %s is already exist", tag1.getName());
        when(tagRepository.readOneByName(tag1.getName())).thenReturn(Optional.of(tag1));
        when(mapper.dtoToModel(tagDto1)).thenReturn(tag1);
        tag1.setActive(true);
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.create(tagDto1));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(tagValidator, only()).createValidate(tagDto1);
        verify(tagRepository, only()).readOneByName(tag1.getName());
        verify(mapper, only()).dtoToModel(tagDto1);
    }

    @Test
    void shouldReturnTag_On_ReadOne() {
        //Given
        when(tagRepository.readOne(tag1.getId(), null)).thenReturn(Optional.of(tag1));
        when(mapper.modelToDto(tag1)).thenReturn(tagDto1);
        //When
        final TagDto actualTag = tagService.readOne(tag1.getId(), null);
        //Then
        assertEquals(tagDto1, actualTag);
        verify(tagRepository, only()).readOne(tag1.getId(), null);
        verify(mapper, only()).modelToDto(tag1);
    }

    @Test
    void shouldThrowException_On_ReadOne_NotFound() {
        //Given
        dummyRb.setMessage("tag.notFound.id", "Tag with id = %s not found");
        final String message = String.format("Tag with id = %s not found", tag1.getId());
        when(tagRepository.readOne(tag1.getId(), null)).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.readOne(tag1.getId(), null));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(tagRepository, only()).readOne(tag1.getId(), null);
    }

    @Test
    void shouldReturnTags_On_ReadAll() {
        //Given
        final List<Tag> tags = Arrays.asList(tag1, tag2);
        final List<TagDto> expectedTags = Arrays.asList(tagDto1, tagDto2);
        //When
        when(tagRepository.readAll(null, null, null)).thenReturn(tags);
        when(mapper.modelsToDto(tags)).thenReturn(expectedTags);
        final List<TagDto> actualTags = tagService.readAll(null, null, null);
        //Then
        assertEquals(expectedTags, actualTags);
        verify(tagRepository, only()).readAll(null, null, null);
        verify(paginationValidator, only()).paginationValidate( null, null);
        verify(mapper, only()).modelsToDto(tags);
    }

    @Test
    void shouldDeleteTag_On_DeleteById() {
        //Given
        when(tagRepository.readOne(tag1.getId(), true)).thenReturn(Optional.of(tag1));
        //When
        tagService.deleteById(tag1.getId());
        //Then
        verify(tagRepository, times(1)).readOne(tag1.getId(), true);
        verify(tagRepository, times(1)).deleteById(tag1.getId());
        verifyNoMoreInteractions(tagRepository);
    }
}