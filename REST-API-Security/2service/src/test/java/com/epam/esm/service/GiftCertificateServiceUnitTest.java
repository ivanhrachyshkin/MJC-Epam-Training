package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.AuthorityValidator;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.PageValidator;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceUnitTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private DtoMapper<GiftCertificate, GiftCertificateDto> mapper;
    @Mock
    private GiftCertificateValidator giftCertificateValidator;
    @Mock
    private PageValidator pageValidator;
    @Mock
    private AuthorityValidator authorityValidator;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;


    @Mock
    private GiftCertificate giftCertificate;
    @Mock
    private GiftCertificate giftCertificate2;
    @Mock
    private GiftCertificateDto dtoGiftCertificate;
    @Mock
    private GiftCertificateDto dtoGiftCertificate2;
    @Mock
    private Page<GiftCertificate> giftCertificates;
    @Mock
    private Page<GiftCertificateDto> dtoGiftCertificates;
    @Mock
    private Tag tag;
    @Mock
    private Tag tag2;

    @Mock
    private Pageable page;
    @Mock
    List<String> tags;
    @Mock
    GiftCertificateRequestParamsContainer container;


    private ResourceBundle rb;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(giftCertificateService, "rb", rb);
    }

    @Test
    void shouldReturnCreatedGiftCertificate_On_CreateNewGiftCertificateWithNewTag() {
        //Given
        giftCertificate = new GiftCertificate();
        giftCertificate.setTags(new HashSet<>(Collections.singletonList(tag)));
        when(mapper.dtoToModel(dtoGiftCertificate)).thenReturn(giftCertificate);
        when(tagRepository.findByName(tag.getName())).thenReturn(Optional.empty());
        when(giftCertificateRepository.findByName(giftCertificate.getName())).thenReturn(Optional.empty());
        when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate2);
        when(mapper.modelToDto(giftCertificate2)).thenReturn(dtoGiftCertificate2);
        //When
        final GiftCertificateDto actualGiftCertificate = giftCertificateService.create(dtoGiftCertificate);
        //Then
        assertEquals(dtoGiftCertificate2, actualGiftCertificate);
        verify(giftCertificateValidator, only()).createValidate(dtoGiftCertificate);
        verify(mapper, times(1)).dtoToModel(dtoGiftCertificate);
        verify(tagRepository, times(1)).findByName(tag.getName());
        verify(tagRepository, times(1)).save(tag);
        verify(giftCertificateRepository, times(1)).findByName(giftCertificate.getName());
        verify(giftCertificateRepository, times(1)).save(giftCertificate);
        verify(mapper, times(1)).modelToDto(giftCertificate2);
        verifyNoMoreInteractions(giftCertificateRepository);
        verifyNoMoreInteractions(tagRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldReturnCreatedGiftCertificate_On_CreateNewGiftCertificateWithOldTag() {
        //Given
        giftCertificate = new GiftCertificate();
        giftCertificate.setTags(new HashSet<>(Collections.singletonList(tag)));
        when(mapper.dtoToModel(dtoGiftCertificate)).thenReturn(giftCertificate);
        when(tagRepository.findByName(tag.getName())).thenReturn(Optional.of(tag));
        when(giftCertificateRepository.findByName(giftCertificate.getName())).thenReturn(Optional.empty());
        when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate2);
        when(mapper.modelToDto(giftCertificate2)).thenReturn(dtoGiftCertificate2);
        //When
        final GiftCertificateDto actualGiftCertificate = giftCertificateService.create(dtoGiftCertificate);
        //Then
        assertEquals(dtoGiftCertificate2, actualGiftCertificate);
        verify(giftCertificateValidator, only()).createValidate(dtoGiftCertificate);
        verify(mapper, times(1)).dtoToModel(dtoGiftCertificate);
        verify(tagRepository, times(1)).findByName(tag.getName());
        verify(giftCertificateRepository, times(1)).findByName(giftCertificate.getName());
        verify(giftCertificateRepository, times(1)).save(giftCertificate);
        verify(mapper, times(1)).modelToDto(giftCertificate2);
        verifyNoMoreInteractions(giftCertificateRepository);
        verifyNoMoreInteractions(tagRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowServiceException_On_Create() {
        //Given
        final String message = String.format(rb.getString("giftCertificate.alreadyExists.name"), giftCertificate.getName());
        giftCertificate = new GiftCertificate();
        giftCertificate.setActive(true);
        giftCertificate.setTags(new HashSet<>(Collections.singletonList(tag)));
        when(mapper.dtoToModel(dtoGiftCertificate)).thenReturn(giftCertificate);
        when(tagRepository.findByName(tag.getName())).thenReturn(Optional.of(tag));
        when(giftCertificateRepository.findByName(giftCertificate.getName())).thenReturn(Optional.of(giftCertificate));
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> giftCertificateService.create(dtoGiftCertificate));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(giftCertificateValidator, only()).createValidate(dtoGiftCertificate);
        verify(mapper, only()).dtoToModel(dtoGiftCertificate);
        verify(tagRepository, only()).findByName(tag.getName());
        verify(giftCertificateRepository, times(1)).findByName(giftCertificate.getName());
        verifyNoMoreInteractions(giftCertificateRepository);
        verifyNoMoreInteractions(tagRepository);
    }


    @Test
    void shouldReturnGiftCertificates_On_ReadAll_For_Admin() {
//todo
    }

    @Test
    void shouldReturnGiftCertificates_On_ReadAll_For_User() {
//todo
    }

    @Test
    void shouldReturnGiftCertificate_On_ReadOne_For_User() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(giftCertificateRepository.findByIdAndActive(1, true)).thenReturn(Optional.of(giftCertificate));
        when(mapper.modelToDto(giftCertificate)).thenReturn(dtoGiftCertificate);
        //When
        final GiftCertificateDto actualGiftCertificateDto = giftCertificateService.readOne(1);
        //Then
        assertEquals(dtoGiftCertificate, actualGiftCertificateDto);
        verify(authorityValidator, only()).isAdmin();
        verify(giftCertificateRepository, only()).findByIdAndActive(1, true);
        verify(mapper, only()).modelToDto(giftCertificate);
    }

    @Test
    void shouldThrowException_On_ReadOne_For_User() {
        //Given
        final String message = String.format(rb.getString("giftCertificate.notFound.id"), 1);
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(giftCertificateRepository.findByIdAndActive(1, true)).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> giftCertificateService.readOne(1));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(giftCertificateRepository, only()).findByIdAndActive(1, true);
    }

    @Test
    void shouldReturnTag_On_ReadOne_For_Admin() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.of(giftCertificate));
        when(mapper.modelToDto(giftCertificate)).thenReturn(dtoGiftCertificate);
        //When
        final GiftCertificateDto actualGiftCertificateDto = giftCertificateService.readOne(1);
        //Then
        assertEquals(dtoGiftCertificate, actualGiftCertificateDto);
        verify(authorityValidator, only()).isAdmin();
        verify(giftCertificateRepository, only()).findById(1);
        verify(mapper, only()).modelToDto(giftCertificate);
    }

    @Test
    void shouldThrowException_On_ReadOne_For_Admin() {
        //Given
        final String message = String.format(rb.getString("giftCertificate.notFound.id"), 1);
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> giftCertificateService.readOne(1));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(giftCertificateRepository, only()).findById(1);
    }

    @Test
    void shouldReturnUpdatedGiftCertificate_On_Update() {
        //Given
        giftCertificate = new GiftCertificate(1);
        giftCertificate2 = new GiftCertificate(1);
        giftCertificate.setTags(new HashSet<>(Collections.singletonList(tag)));
        giftCertificate2.setTags(new HashSet<>(Collections.singletonList(tag2)));

        when(giftCertificateRepository.findByName(giftCertificate.getName())).thenReturn(Optional.empty());
        when(mapper.dtoToModel(dtoGiftCertificate)).thenReturn(giftCertificate);
        when(giftCertificateRepository.findById(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        when(tagRepository.findByName(tag.getName())).thenReturn(Optional.of(tag));
        when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate2);
        when(mapper.modelToDto(giftCertificate2)).thenReturn(dtoGiftCertificate2);
        //When
        final GiftCertificateDto actualGiftCertificate = giftCertificateService.update(dtoGiftCertificate);
        //Then
        assertEquals(dtoGiftCertificate2, actualGiftCertificate);
        verify(giftCertificateValidator, only()).updateValidate(dtoGiftCertificate);
        verify(giftCertificateRepository, times(1)).findByName(giftCertificate.getName());
        verify(mapper, times(1)).dtoToModel(dtoGiftCertificate);
        verify(giftCertificateRepository, times(1)).findById(giftCertificate.getId());
        verify(tagRepository, only()).findByName(tag.getName());
        verify(giftCertificateRepository, times(1)).save(giftCertificate);
        verify(mapper, times(1)).modelToDto(giftCertificate2);
        verifyNoMoreInteractions(giftCertificateRepository);
        verifyNoMoreInteractions(tagRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowServiceException_On_Update() {
        //Given
        final String message = String.format(rb.getString("giftCertificate.alreadyExists.name"), dtoGiftCertificate.getName());
        when(giftCertificateRepository.findByName(dtoGiftCertificate.getName())).thenReturn(Optional.of(giftCertificate));
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> giftCertificateService.update(dtoGiftCertificate));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(giftCertificateValidator, only()).updateValidate(dtoGiftCertificate);
        verify(giftCertificateRepository, only()).findByName(giftCertificate.getName());
    }


    @Test
    void shouldDelete_On_Delete() {
        //Given
        when(giftCertificateRepository.findByIdAndActive(1, true)).thenReturn(Optional.of(giftCertificate));
        //When
        giftCertificateService.deleteById(1);
        //Then
        verify(giftCertificateValidator, only()).validateId(1);
        verify(giftCertificateRepository, times(1)).findByIdAndActive(1, true);
        verify(giftCertificateRepository, times(1)).save(giftCertificate);
        verifyNoMoreInteractions(giftCertificateRepository);
    }

    @Test
    void shouldThrowException_On_Delete() {
        //Given
        final String message = String.format(rb.getString("giftCertificate.notFound.id"), 1);
        when(giftCertificateRepository.findByIdAndActive(1, true)).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> giftCertificateService.deleteById(1));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(giftCertificateValidator, only()).validateId(1);
        verify(giftCertificateRepository, only()).findByIdAndActive(1, true);
    }
}
