package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.Tag;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.AuthorityValidator;
import com.epam.esm.service.validator.PageableValidator;
import com.epam.esm.service.validator.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private DtoMapper<Tag, TagDto> mapper;
    @Mock
    private TagValidator tagValidator;
    @Mock
    private PageableValidator pageableValidator;
    @Mock
    private AuthorityValidator authorityValidator;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private Tag inputTag;
    @Mock
    private Tag outputTag;
    @Mock
    private TagDto inputDtoTag;
    @Mock
    private TagDto outputDtoTag;
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
        when(mapper.dtoToModel(inputDtoTag)).thenReturn(inputTag);
        when(tagRepository.findByName(inputTag.getName())).thenReturn(Optional.empty());
        when(tagRepository.save(inputTag)).thenReturn(outputTag);
        when(mapper.modelToDto(outputTag)).thenReturn(outputDtoTag);
        //When
        final TagDto actualDtoTag = tagService.create(inputDtoTag);
        //Then
        assertEquals(outputDtoTag, actualDtoTag);
        verify(tagValidator, only()).validate(inputDtoTag);
        verify(tagRepository, times(1)).findByName(inputTag.getName());
        verify(tagRepository, times(1)).save(inputTag);
        verifyNoMoreInteractions(tagRepository);
        verify(mapper, times(1)).dtoToModel(inputDtoTag);
        verify(mapper, times(1)).modelToDto(outputTag);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldReturnCreatedTag_On_CreateOld() {
        //Given
        when(mapper.dtoToModel(inputDtoTag)).thenReturn(inputTag);
        when(tagRepository.findByName(inputTag.getName())).thenReturn(Optional.of(inputTag));
        when(tagRepository.save(inputTag)).thenReturn(outputTag);
        when(mapper.modelToDto(outputTag)).thenReturn(outputDtoTag);
        //When
        final TagDto actualDtoTag = tagService.create(inputDtoTag);
        //Then
        assertEquals(outputDtoTag, actualDtoTag);
        verify(tagValidator, only()).validate(inputDtoTag);
        verify(tagRepository, times(1)).findByName(inputTag.getName());
        verify(tagRepository, times(1)).save(inputTag);
        verifyNoMoreInteractions(tagRepository);
        verify(mapper, times(1)).dtoToModel(inputDtoTag);
        verify(mapper, times(1)).modelToDto(outputTag);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowServiceException_On_Create() {
        //Given
        when(inputTag.getActive()).thenReturn(true);
        when(mapper.dtoToModel(inputDtoTag)).thenReturn(inputTag);
        when(tagRepository.findByName(inputTag.getName())).thenReturn(Optional.of(inputTag));
        final ServiceException expectedException = new ServiceException(
                rb.getString("tag.alreadyExists.name"),
                HttpStatus.CONFLICT, properties.getTag(), inputTag.getName());
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> tagService.create(inputDtoTag));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(mapper, times(1)).dtoToModel(inputDtoTag);
        verify(tagValidator, only()).validate(inputDtoTag);
        verify(tagRepository, only()).findByName(inputTag.getName());
    }

    @Test
    void shouldReturnTags_On_ReadAll_For_Admin() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(tagRepository.findAll(page)).thenReturn(tags);
        when(mapper.modelsToDto(tags)).thenReturn(dtoTags);
        //When
        final Page<TagDto> actualDtoTags = tagService.readAll(page);
        //Then
        assertPages(dtoTags, actualDtoTags);
        assertEquals(dtoTags.getTotalElements(), actualDtoTags.getTotalElements());
        assertEquals(dtoTags.getTotalPages(), actualDtoTags.getTotalPages());
        verify(pageableValidator, only()).paginationValidate(page);
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
        final Page<TagDto> actualDtoTags = tagService.readAll(page);
        //Then
        assertPages(dtoTags, actualDtoTags);
        assertEquals(dtoTags.getTotalElements(), actualDtoTags.getTotalElements());
        assertEquals(dtoTags.getTotalPages(), actualDtoTags.getTotalPages());
        verify(pageableValidator, only()).paginationValidate(page);
        verify(authorityValidator, only()).isAdmin();
        verify(tagRepository, only()).findAllByActive(true, page);
        verify(mapper, only()).modelsToDto(tags);
    }

    @Test
    void shouldReturnTag_On_ReadOne_For_User() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(tagRepository.findByIdAndActive(1, true)).thenReturn(Optional.of(inputTag));
        when(mapper.modelToDto(inputTag)).thenReturn(inputDtoTag);
        //When
        final TagDto actualDtoTag = tagService.readOne(1);
        //Then
        assertEquals(inputDtoTag, actualDtoTag);
        verify(authorityValidator, only()).isAdmin();
        verify(tagRepository, only()).findByIdAndActive(1, true);
        verify(mapper, only()).modelToDto(inputTag);
    }

    @Test
    void shouldThrowException_On_ReadOne_For_User() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(tagRepository.findByIdAndActive(1, true)).thenReturn(Optional.empty());
        final ServiceException expectedException = new ServiceException(
                rb.getString("tag.notFound.id"),
                HttpStatus.NOT_FOUND, properties.getTag(), 1);
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> tagService.readOne(1));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(tagRepository, only()).findByIdAndActive(1, true);
    }

    @Test
    void shouldReturnTag_On_ReadOne_For_Admin() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(tagRepository.findById(1)).thenReturn(Optional.of(inputTag));
        when(mapper.modelToDto(inputTag)).thenReturn(inputDtoTag);
        //When
        final TagDto actualDtoTag = tagService.readOne(1);
        //Then
        assertEquals(inputDtoTag, actualDtoTag);
        verify(authorityValidator, only()).isAdmin();
        verify(tagRepository, only()).findById(1);
        verify(mapper, only()).modelToDto(inputTag);
    }

    @Test
    void shouldThrowException_On_ReadOne_For_Admin() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(tagRepository.findById(1)).thenReturn(Optional.empty());
        final ServiceException expectedException = new ServiceException(
                rb.getString("tag.notFound.id"),
                HttpStatus.NOT_FOUND, properties.getTag(), 1);
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> tagService.readOne(1));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(tagRepository, only()).findById(1);
    }

    @Test
    void shouldReturnMostUsed_On_ReadMostUsed() {
        //Given
        when(tagRepository.readMostUsed(page)).thenReturn(tags);
        when(mapper.modelsToDto(tags)).thenReturn(dtoTags);
        //When
        final Page<TagDto> actualDtoTags = tagService.readMostUsed(page);
        //Then
        assertPages(dtoTags, actualDtoTags);
        assertEquals(dtoTags.getTotalElements(), actualDtoTags.getTotalElements());
        assertEquals(dtoTags.getTotalPages(), actualDtoTags.getTotalPages());
        verify(tagRepository, only()).readMostUsed(page);
        verify(mapper, only()).modelsToDto(tags);
    }

    @Test
    void shouldDelete_On_Delete_() {
        //Given
        when(tagRepository.findByIdAndActive(1, true)).thenReturn(Optional.of(inputTag));
        when(tagRepository.save(inputTag)).thenReturn(outputTag);
        when(mapper.modelToDto(outputTag)).thenReturn(outputDtoTag);
        //When
        final TagDto actualTagDto = tagService.deleteById(1);
        //Then
        assertEquals(outputDtoTag, actualTagDto);
        verify(tagValidator, only()).validateId(1);
        verify(tagRepository, times(1)).findByIdAndActive(1, true);
        verify(tagRepository, times(1)).save(inputTag);
        verify(mapper, only()).modelToDto(outputTag);
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    void shouldThrowException_On_Delete() {
        //Given
        when(tagRepository.findByIdAndActive(1, true)).thenReturn(Optional.empty());
        final ServiceException expectedException = new ServiceException(
                rb.getString("tag.notFound.id"),
                HttpStatus.NOT_FOUND, properties.getTag(), 1);
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> tagService.deleteById(1));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(tagValidator, only()).validateId(1);
        verify(tagRepository, only()).findByIdAndActive(1, true);
    }
}
