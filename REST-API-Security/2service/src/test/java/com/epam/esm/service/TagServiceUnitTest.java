package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.Tag;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.AuthorityValidator;
import com.epam.esm.service.validator.PageValidator;
import com.epam.esm.service.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceUnitTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private DtoMapper<Tag, TagDto> mapper;
    @Mock
    private TagValidator tagValidator;
    @Mock
    private PageValidator pageValidator;
    @Mock
    private AuthorityValidator authorityValidator;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private Tag tag;
    @Mock
    private Tag tag2;
    @Mock
    private TagDto dtoTag;
    @Mock
    private TagDto dtoTag2;
    @Mock
    private Page<Tag> tags;
    @Mock
    private Page<TagDto> dtoTags;
    @Mock
    private Pageable page;

    private ResourceBundle rb;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(tagService, "rb", rb);
    }

    @Test
    void shouldReturnCreatedTag_On_CreateNew() {
        //Given
        when(mapper.dtoToModel(dtoTag)).thenReturn(tag);
        when(tagRepository.findByName(tag.getName())).thenReturn(Optional.empty());
        when(tagRepository.save(tag)).thenReturn(tag2);
        when(mapper.modelToDto(tag2)).thenReturn(dtoTag2);
        //When
        final TagDto actualTag = tagService.create(dtoTag);
        //Then
        assertEquals(dtoTag2, actualTag);
        verify(tagValidator, only()).validate(dtoTag);
        verify(tagRepository, times(1)).findByName(tag.getName());
        verify(tagRepository, times(1)).save(tag);
        verifyNoMoreInteractions(tagRepository);
        verify(mapper, times(1)).dtoToModel(dtoTag);
        verify(mapper, times(1)).modelToDto(tag2);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldReturnCreatedTag_On_CreateOld() {
        //Given
        when(mapper.dtoToModel(dtoTag)).thenReturn(tag);
        when(tagRepository.findByName(tag.getName())).thenReturn(Optional.of(tag));
        when(tagRepository.save(tag)).thenReturn(tag2);
        when(mapper.modelToDto(tag2)).thenReturn(dtoTag2);
        //When
        final TagDto actualTag = tagService.create(dtoTag);
        //Then
        assertEquals(dtoTag2, actualTag);
        verify(tagValidator, only()).validate(dtoTag);
        verify(tagRepository, times(1)).findByName(tag.getName());
        verify(tagRepository, times(1)).save(tag);
        verifyNoMoreInteractions(tagRepository);
        verify(mapper, times(1)).dtoToModel(dtoTag);
        verify(mapper, times(1)).modelToDto(tag2);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowServiceException_On_Create() {
        //Given
        tag = new Tag();
        tag.setActive(true);
        final String message = String.format(rb.getString("tag.alreadyExists.name"), tag.getName());
        when(mapper.dtoToModel(dtoTag)).thenReturn(tag);
        when(tagRepository.findByName(tag.getName())).thenReturn(Optional.of(tag));
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.create(dtoTag));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(mapper, times(1)).dtoToModel(dtoTag);
        verify(tagValidator, only()).validate(dtoTag);
        verify(tagRepository, only()).findByName(tag.getName());
    }

    @Test
    void shouldReturnTags_On_ReadAll_For_Admin() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(tagRepository.findAll(page)).thenReturn(tags);
        when(mapper.modelsToDto(tags)).thenReturn(dtoTags);
        //When
        final Page<TagDto> actualTags = tagService.readAll(page);
        //Then
        assertEquals(dtoTags, actualTags);
        verify(pageValidator, only()).paginationValidate(page);
        verify(authorityValidator, only()).isAdmin();
        verify(tagRepository, only()).findAll(page);
        verify(mapper, only()).modelsToDto(tags);
    }

    @Test
    void shouldReturnTags_On_ReadAll_For_User() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(tagRepository.findAllByActive(true, page)).thenReturn(tags);
        when(mapper.modelsToDto(tags)).thenReturn(dtoTags);
        //When
        final Page<TagDto> actualTags = tagService.readAll(page);
        //Then
        assertEquals(dtoTags.getTotalElements(), actualTags.getTotalElements());
        assertEquals(dtoTags.getTotalPages(), actualTags.getTotalPages());
        verify(pageValidator, only()).paginationValidate(page);
        verify(authorityValidator, only()).isAdmin();
        verify(tagRepository, only()).findAllByActive(true, page);
        verify(mapper, only()).modelsToDto(tags);
    }

    @Test
    void shouldReturnTag_On_ReadOne_For_User() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(tagRepository.findByIdAndActive(1,true)).thenReturn(Optional.of(tag));
        when(mapper.modelToDto(tag)).thenReturn(dtoTag);
        //When
        final TagDto actualTag = tagService.readOne(1);
        //Then
        assertEquals(dtoTag, actualTag);
        verify(authorityValidator, only()).isAdmin();
        verify(tagRepository, only()).findByIdAndActive(1, true);
        verify(mapper, only()).modelToDto(tag);
    }

    @Test
    void shouldThrowException_On_ReadOne_For_User() {
        //Given
        final String message = String.format(rb.getString("tag.notFound.id"), 1);
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(tagRepository.findByIdAndActive(1,true)).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.readOne(1));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(tagRepository, only()).findByIdAndActive(1,true);
    }

    @Test
    void shouldReturnTag_On_ReadOne_For_Admin() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(tagRepository.findById(1)).thenReturn(Optional.of(tag));
        when(mapper.modelToDto(tag)).thenReturn(dtoTag);
        //When
        final TagDto actualTag = tagService.readOne(1);
        //Then
        assertEquals(dtoTag, actualTag);
        verify(authorityValidator, only()).isAdmin();
        verify(tagRepository, only()).findById(1);
        verify(mapper, only()).modelToDto(tag);
    }

    @Test
    void shouldThrowException_On_ReadOne_For_Admin() {
        //Given
        final String message = String.format(rb.getString("tag.notFound.id"), 1);
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(tagRepository.findById(1)).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.readOne(1));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(tagRepository, only()).findById(1);
    }

    @Test
    void shouldReturnMostUsed_On_ReadMostUsed() {
        //Given
        when(tagRepository.readMostUsed(page)).thenReturn(tags);
        when(mapper.modelsToDto(tags)).thenReturn(dtoTags);
        //When
        final Page<TagDto> actualTags = tagService.readMostUsed(page);
        //Then
        assertEquals(dtoTags.getTotalElements(), actualTags.getTotalElements());
        assertEquals(dtoTags.getTotalPages(), actualTags.getTotalPages());
        verify(tagRepository, only()).readMostUsed(page);
        verify(mapper, only()).modelsToDto(tags);
    }

    @Test
    void shouldDelete_On_Delete_() {
        //Given
        when(tagRepository.findByIdAndActive(1, true)).thenReturn(Optional.of(tag));
        //When
        tagService.deleteById(1);
        //Then
        verify(tagValidator, only()).validateId(1);
        verify(tagRepository, times(1)).findByIdAndActive(1, true);
        verify(tagRepository, times(1)).save(tag);
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    void shouldThrowException_On_Delete() {
        //Given
        final String message = String.format(rb.getString("tag.notFound.id"), 1);
        when(tagRepository.findByIdAndActive(1, true)).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.deleteById(1));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(tagValidator, only()).validateId(1);
        verify(tagRepository, only()).findByIdAndActive(1, true);
    }
}
