package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.GiftCertificateSpecification;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest extends AssertionsProvider<GiftCertificateDto> {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private TagServiceImpl tagService;
    @Mock
    private DtoMapper<GiftCertificate, GiftCertificateDto> mapper;
    @Mock
    private GiftCertificateValidator giftCertificateValidator;
    @Mock
    private PageValidator pageValidator;
    @Mock
    private GiftCertificateSpecification specification;
    @Mock
    private AuthorityValidator authorityValidator;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @Mock
    private HttpServletRequest request;
    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Mock
    private GiftCertificate inputGiftCertificate;
    @Mock
    private GiftCertificate outputGiftCertificate;
    @Mock
    private GiftCertificateDto dtoInputGiftCertificate;
    @Mock
    private GiftCertificateDto dtoOutputGiftCertificate;
    @Mock
    private Page<GiftCertificate> outputGiftCertificates;
    @Mock
    private Page<GiftCertificateDto> dtoOutputGiftCertificates;

    @Mock
    private Tag inputTag;

    @Mock
    private Pageable page;
    @Mock
    private GiftCertificateRequestParamsContainer container;


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
        when(inputGiftCertificate.getTags()).thenReturn(Collections.singleton(inputTag));
        when(request.getMethod()).thenReturn(HttpMethod.POST.name());
        when(mapper.dtoToModel(dtoInputGiftCertificate)).thenReturn(inputGiftCertificate);
        when(giftCertificateRepository.findByName(inputGiftCertificate.getName())).thenReturn(Optional.empty());
        when(giftCertificateRepository.save(inputGiftCertificate)).thenReturn(outputGiftCertificate);
        when(mapper.modelToDto(outputGiftCertificate)).thenReturn(dtoOutputGiftCertificate);
        //When
        final GiftCertificateDto actualDtoGiftCertificate = giftCertificateService.create(dtoInputGiftCertificate);
        //Then
        assertEquals(dtoOutputGiftCertificate, actualDtoGiftCertificate);
        verify(giftCertificateValidator, only()).validateCreateOrUpdate(dtoInputGiftCertificate, HttpMethod.POST.name());
        verify(mapper, times(1)).dtoToModel(dtoInputGiftCertificate);
        verify(tagService, only()).prepareTagsForGiftCertificate(inputGiftCertificate.getTags());
        verify(giftCertificateRepository, times(1)).findByName(inputGiftCertificate.getName());
        verify(giftCertificateRepository, times(1)).save(inputGiftCertificate);
        verify(mapper, times(1)).modelToDto(outputGiftCertificate);
        verifyNoMoreInteractions(giftCertificateRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldReturnCreatedGiftCertificate_On_CreateNewGiftCertificateWithOldTag() {
        //Given
        when(inputGiftCertificate.getTags()).thenReturn(Collections.singleton(inputTag));
        when(request.getMethod()).thenReturn(HttpMethod.POST.name());
        when(mapper.dtoToModel(dtoInputGiftCertificate)).thenReturn(inputGiftCertificate);
        when(giftCertificateRepository.findByName(inputGiftCertificate.getName())).thenReturn(Optional.empty());
        when(giftCertificateRepository.save(inputGiftCertificate)).thenReturn(outputGiftCertificate);
        when(mapper.modelToDto(outputGiftCertificate)).thenReturn(dtoOutputGiftCertificate);
        //When
        final GiftCertificateDto actualDtoGiftCertificate = giftCertificateService.create(dtoInputGiftCertificate);
        //Then
        assertEquals(dtoOutputGiftCertificate, actualDtoGiftCertificate);
        verify(giftCertificateValidator, only()).validateCreateOrUpdate(dtoInputGiftCertificate, HttpMethod.POST.name());
        verify(mapper, times(1)).dtoToModel(dtoInputGiftCertificate);
        verify(tagService, only()).prepareTagsForGiftCertificate(inputGiftCertificate.getTags());
        verify(giftCertificateRepository, times(1)).findByName(inputGiftCertificate.getName());
        verify(giftCertificateRepository, times(1)).save(inputGiftCertificate);
        verify(mapper, times(1)).modelToDto(outputGiftCertificate);
        verifyNoMoreInteractions(giftCertificateRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowServiceException_On_Create() {
        //Given
        when(inputGiftCertificate.getActive()).thenReturn(true);
        when(inputGiftCertificate.getTags()).thenReturn(Collections.singleton(inputTag));
        when(request.getMethod()).thenReturn(HttpMethod.POST.name());
        when(mapper.dtoToModel(dtoInputGiftCertificate)).thenReturn(inputGiftCertificate);
        when(giftCertificateRepository.findByName(inputGiftCertificate.getName())).thenReturn(Optional.of(inputGiftCertificate));
        final ServiceException expectedException = new ServiceException(
                rb.getString("giftCertificate.alreadyExists.name"),
                HttpStatus.CONFLICT, properties.getGift(), inputGiftCertificate.getName());
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> giftCertificateService.create(dtoInputGiftCertificate));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(giftCertificateValidator, only()).validateCreateOrUpdate(dtoInputGiftCertificate, HttpMethod.POST.name());
        verify(mapper, only()).dtoToModel(dtoInputGiftCertificate);
        verify(tagService, only()).prepareTagsForGiftCertificate(inputGiftCertificate.getTags());
        verify(giftCertificateRepository, times(1)).findByName(inputGiftCertificate.getName());
        verifyNoMoreInteractions(giftCertificateRepository);
    }

    @Test
    void shouldReturnGiftCertificates_On_ReadAll_For_Admin() {
        //Given
        when(authorityValidator.isNotAdmin()).thenReturn(true);
        when(container.getName()).thenReturn("name");
        when(container.getDescription()).thenReturn("desc");
        when(giftCertificateRepository
                .findAll(specification
                        .giftCertificateFiltered(
                                Collections.emptyList(), "name", "desc", true), page))
                .thenReturn(outputGiftCertificates);
        when(mapper.modelsToDto(outputGiftCertificates)).thenReturn(dtoOutputGiftCertificates);
        //When
        final Page<GiftCertificateDto> actualDtoGiftCertificates
                = giftCertificateService.readAll(Collections.emptyList(), container, page);
        //Then
        assertPages(dtoOutputGiftCertificates, actualDtoGiftCertificates);
        verify(giftCertificateValidator, only()).readAllValidate(Collections.emptyList(), container);
        verify(pageValidator, only()).paginationValidate(page);
        verify(mapper, only()).modelsToDto(outputGiftCertificates);
    }

    @Test
    void shouldReturnGiftCertificates_On_ReadAll_For_User() {
        //Given
        when(authorityValidator.isNotAdmin()).thenReturn(false);
        when(container.getName()).thenReturn("name");
        when(container.getDescription()).thenReturn("desc");
        when(giftCertificateRepository
                .findAll(specification
                        .giftCertificateFiltered(
                                Collections.emptyList(), "name", "desc", false), page))
                .thenReturn(outputGiftCertificates);
        when(mapper.modelsToDto(outputGiftCertificates)).thenReturn(dtoOutputGiftCertificates);
        //When
        final Page<GiftCertificateDto> actualDtoGiftCertificates
                = giftCertificateService.readAll(Collections.emptyList(), container, page);
        //Then
        assertPages(dtoOutputGiftCertificates, actualDtoGiftCertificates);
        verify(giftCertificateValidator, only()).readAllValidate(Collections.emptyList(), container);
        verify(pageValidator, only()).paginationValidate(page);
        verify(mapper, only()).modelsToDto(outputGiftCertificates);
    }

    @Test
    void shouldReturnGiftCertificate_On_ReadOne_For_User() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(giftCertificateRepository.findByIdAndActive(1, true))
                .thenReturn(Optional.of(outputGiftCertificate));
        when(mapper.modelToDto(outputGiftCertificate)).thenReturn(dtoOutputGiftCertificate);
        //When
        final GiftCertificateDto actualDtoGiftCertificate = giftCertificateService.readOne(1);
        //Then
        assertEquals(dtoOutputGiftCertificate, actualDtoGiftCertificate);
        verify(authorityValidator, only()).isAdmin();
        verify(giftCertificateRepository, only()).findByIdAndActive(1, true);
        verify(mapper, only()).modelToDto(outputGiftCertificate);
    }

    @Test
    void shouldThrowException_On_ReadOne_For_User() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(giftCertificateRepository.findByIdAndActive(1, true)).thenReturn(Optional.empty());
        final ServiceException expectedException = new ServiceException(
                rb.getString("giftCertificate.notFound.id"),
                HttpStatus.NOT_FOUND, properties.getGift(), 1);
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> giftCertificateService.readOne(1));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(giftCertificateRepository, only()).findByIdAndActive(1, true);
    }

    @Test
    void shouldReturnTag_On_ReadOne_For_Admin() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.of(outputGiftCertificate));
        when(mapper.modelToDto(outputGiftCertificate)).thenReturn(dtoOutputGiftCertificate);
        //When
        final GiftCertificateDto actualDtoGiftCertificate = giftCertificateService.readOne(1);
        //Then
        assertEquals(dtoOutputGiftCertificate, actualDtoGiftCertificate);
        verify(authorityValidator, only()).isAdmin();
        verify(giftCertificateRepository, only()).findById(1);
        verify(mapper, only()).modelToDto(outputGiftCertificate);
    }

    @Test
    void shouldThrowException_On_ReadOne_For_Admin() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.empty());
        final ServiceException expectedException = new ServiceException(
                rb.getString("giftCertificate.notFound.id"),
                HttpStatus.NOT_FOUND, properties.getGift(), 1);
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> giftCertificateService.readOne(1));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(giftCertificateRepository, only()).findById(1);
    }

    @Test
    void shouldReturnUpdatedGiftCertificate_On_Update() {
        //Given
        when(inputGiftCertificate.getTags()).thenReturn(Collections.singleton(inputTag));
        when(giftCertificateRepository.findByName(inputGiftCertificate.getName())).thenReturn(Optional.empty());
        when(request.getMethod()).thenReturn(HttpMethod.PATCH.name());
        when(mapper.dtoToModel(dtoInputGiftCertificate)).thenReturn(inputGiftCertificate);
        when(giftCertificateRepository.findById(inputGiftCertificate.getId())).thenReturn(Optional.of(inputGiftCertificate));
        when(giftCertificateRepository.save(inputGiftCertificate)).thenReturn(outputGiftCertificate);
        when(mapper.modelToDto(outputGiftCertificate)).thenReturn(dtoOutputGiftCertificate);
        //When
        final GiftCertificateDto actualDtoGiftCertificate = giftCertificateService.update(dtoInputGiftCertificate);
        //Then
        assertEquals(dtoOutputGiftCertificate, actualDtoGiftCertificate);
        verify(giftCertificateValidator, only()).validateCreateOrUpdate(dtoInputGiftCertificate, HttpMethod.PATCH.name());
        verify(giftCertificateRepository, times(1)).findByName(inputGiftCertificate.getName());
        verify(mapper, times(1)).dtoToModel(dtoInputGiftCertificate);
        verify(giftCertificateRepository, times(1)).findById(inputGiftCertificate.getId());
        verify(tagService, only()).prepareTagsForGiftCertificate(inputGiftCertificate.getTags());
        verify(giftCertificateRepository, times(1)).save(inputGiftCertificate);
        verify(mapper, times(1)).modelToDto(outputGiftCertificate);
        verifyNoMoreInteractions(giftCertificateRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowServiceException_On_Update() {
        //Given
        when(request.getMethod()).thenReturn(HttpMethod.PATCH.name());
        when(giftCertificateRepository.findById(inputGiftCertificate.getId()))
                .thenReturn(Optional.of(inputGiftCertificate));
        when(giftCertificateRepository.findByName(inputGiftCertificate.getName()))
                .thenReturn(Optional.of(inputGiftCertificate));
        final ServiceException expectedException = new ServiceException(
                rb.getString("giftCertificate.alreadyExists.name"),
                HttpStatus.CONFLICT, properties.getGift(), inputGiftCertificate.getName());
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> giftCertificateService.update(dtoInputGiftCertificate));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(giftCertificateValidator, only()).validateCreateOrUpdate(dtoInputGiftCertificate, HttpMethod.PATCH.name());
        verify(giftCertificateRepository, times(1)).findById(inputGiftCertificate.getId());
        verify(giftCertificateRepository, times(1)).findByName(inputGiftCertificate.getName());
        verifyNoMoreInteractions(giftCertificateRepository);
    }


    @Test
    void shouldDelete_On_Delete() {
        //Given
        when(giftCertificateRepository.findByIdAndActive(1, true)).thenReturn(Optional.of(inputGiftCertificate));
        //When
        giftCertificateService.deleteById(1);
        //Then
        verify(giftCertificateValidator, only()).validateId(1);
        verify(giftCertificateRepository, times(1)).findByIdAndActive(1, true);
        verify(giftCertificateRepository, times(1)).save(inputGiftCertificate);
        verifyNoMoreInteractions(giftCertificateRepository);
    }

    @Test
    void shouldThrowException_On_Delete() {
        //Given
        when(giftCertificateRepository.findByIdAndActive(1, true)).thenReturn(Optional.empty());
        final ServiceException expectedException = new ServiceException(
                rb.getString("giftCertificate.notFound.id"),
                HttpStatus.NOT_FOUND, properties.getGift(), 1);
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> giftCertificateService.deleteById(1));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(giftCertificateValidator, only()).validateId(1);
        verify(giftCertificateRepository, only()).findByIdAndActive(1, true);
    }
}
