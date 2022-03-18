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
    @InjectMocks
    private TagServiceImpl tagService;
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

    @Mock
    private Tag tag1;
    @Mock
    private TagDto tagDto1;
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
    void shouldReturnCreatedTag_On_Create() {
        //Given
        when(mapper.dtoToModel(tagDto1)).thenReturn(tag1);
        when(tagRepository.findByName(tag1.getName())).thenReturn(Optional.of(tag1));
        when(tagRepository.save(tag1)).thenReturn(tag1);
        when(mapper.modelToDto(tag1)).thenReturn(tagDto1);
        //When
        final TagDto actualTag = tagService.create(tagDto1);
        //Then
        assertEquals(tagDto1, actualTag);
        verify(tagValidator, only()).validate(tagDto1);
        verify(tagRepository, times(1)).findByName(tag1.getName());
        verify(tagRepository, times(1)).save(tag1);
        verifyNoMoreInteractions(tagRepository);
        verify(mapper, times(1)).dtoToModel(tagDto1);
        verify(mapper, times(1)).modelToDto(tag1);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowServiceException_On_Create() {
        //Given
        tag1 = new Tag();
        tag1.setIsActive(true);
        final String message = String.format(rb.getString("tag.alreadyExists.name"), tag1.getName());
        when(mapper.dtoToModel(tagDto1)).thenReturn(tag1);
        when(tagRepository.findByName(tag1.getName())).thenReturn(Optional.of(tag1));
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.create(tagDto1));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(mapper, times(1)).dtoToModel(tagDto1);
        verify(tagValidator, only()).validate(tagDto1);
        verify(tagRepository, only()).findByName(tag1.getName());
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
        when(tagRepository.findAllByIsActive(true, page)).thenReturn(tags);
        when(mapper.modelsToDto(tags)).thenReturn(dtoTags);
        //When
        final Page<TagDto> actualTags = tagService.readAll(page);
        //Then
        assertEquals(dtoTags, actualTags);
        verify(pageValidator, only()).paginationValidate(page);
        verify(authorityValidator, only()).isAdmin();
        verify(tagRepository, only()).findAllByIsActive(true, page);
        verify(mapper, only()).modelsToDto(tags);
    }

    @Test
    void shouldReturnTag_On_ReadOne_For_User() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(tagRepository.findByIdAndIsActive(1,true)).thenReturn(Optional.of(tag1));
        when(mapper.modelToDto(tag1)).thenReturn(tagDto1);
        //When
        final TagDto actualTag = tagService.readOne(1);
        //Then
        assertEquals(tagDto1, actualTag);
        verify(authorityValidator, only()).isAdmin();
        verify(tagRepository, only()).findByIdAndIsActive(1, true);
        verify(mapper, only()).modelToDto(tag1);
    }

    @Test
    void shouldThrowException_On_ReadOne_For_User() {
        //Given
        final String message = String.format(rb.getString("tag.notFound.id"), 1);
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(tagRepository.findByIdAndIsActive(1,true)).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> tagService.readOne(1));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(tagRepository, only()).findByIdAndIsActive(1,true);
    }

    @Test
    void shouldReturnTag_On_ReadOne_For_Admin() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(tagRepository.findById(1)).thenReturn(Optional.of(tag1));
        when(mapper.modelToDto(tag1)).thenReturn(tagDto1);
        //When
        final TagDto actualTag = tagService.readOne(1);
        //Then
        assertEquals(tagDto1, actualTag);
        verify(authorityValidator, only()).isAdmin();
        verify(tagRepository, only()).findById(1);
        verify(mapper, only()).modelToDto(tag1);
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
        assertEquals(dtoTags, actualTags);
        verify(tagRepository, only()).readMostUsed(page);
        verify(mapper, only()).modelsToDto(tags);
    }

    @Test
    void shouldReturnTag_On_Delete_() {
        //Given
        tag1 = new Tag();
        tag1.setIsActive(false);
        when(tagRepository.findByIdAndIsActive(1, true)).thenReturn(Optional.of(tag1));
        //When
        tagService.deleteById(1);
        //Then
        verify(tagValidator, only()).validateId(1);
        verify(tagRepository, times(1)).findByIdAndIsActive(1, true);
        verify(tagRepository, times(1)).save(tag1);
        verifyNoMoreInteractions(tagRepository);
    }
}
