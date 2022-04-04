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
import com.epam.esm.service.validator.PageableValidator;
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
    private PageableValidator pageableValidator;
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
    private GiftCertificate inGiftCertificate;
    @Mock
    private GiftCertificate outGiftCertificate;
    @Mock
    private GiftCertificateDto inDtoGiftCertificate;
    @Mock
    private GiftCertificateDto outDtoGiftCertificate;
    @Mock
    private Page<GiftCertificate> outGiftCertificates;
    @Mock
    private Page<GiftCertificateDto> outDtoGiftCertificates;
    @Mock
    private GiftCertificateMapper giftCertificateMapper;

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
        when(inGiftCertificate.getTags()).thenReturn(Collections.singleton(inputTag));
        when(request.getMethod()).thenReturn(HttpMethod.POST.name());
        when(mapper.dtoToModel(inDtoGiftCertificate)).thenReturn(inGiftCertificate);
        when(giftCertificateRepository.findByName(inGiftCertificate.getName())).thenReturn(Optional.empty());
        when(giftCertificateRepository.save(inGiftCertificate)).thenReturn(outGiftCertificate);
        when(mapper.modelToDto(outGiftCertificate)).thenReturn(outDtoGiftCertificate);
        //When
        final GiftCertificateDto actualDtoGiftCertificate = giftCertificateService.create(inDtoGiftCertificate);
        //Then
        assertEquals(outDtoGiftCertificate, actualDtoGiftCertificate);
        verify(giftCertificateValidator, only()).validateCreateOrUpdate(inDtoGiftCertificate, HttpMethod.POST.name());
        verify(mapper, times(1)).dtoToModel(inDtoGiftCertificate);
        verify(tagService, only()).prepareTagsForGiftCertificate(inGiftCertificate.getTags());
        verify(giftCertificateRepository, times(1)).findByName(inGiftCertificate.getName());
        verify(giftCertificateRepository, times(1)).save(inGiftCertificate);
        verify(mapper, times(1)).modelToDto(outGiftCertificate);
        verifyNoMoreInteractions(giftCertificateRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldReturnCreatedGiftCertificate_On_CreateNewGiftCertificateWithOldTag() {
        //Given
        when(inGiftCertificate.getTags()).thenReturn(Collections.singleton(inputTag));
        when(request.getMethod()).thenReturn(HttpMethod.POST.name());
        when(mapper.dtoToModel(inDtoGiftCertificate)).thenReturn(inGiftCertificate);
        when(giftCertificateRepository.findByName(inGiftCertificate.getName())).thenReturn(Optional.empty());
        when(giftCertificateRepository.save(inGiftCertificate)).thenReturn(outGiftCertificate);
        when(mapper.modelToDto(outGiftCertificate)).thenReturn(outDtoGiftCertificate);
        //When
        final GiftCertificateDto actualDtoGiftCertificate = giftCertificateService.create(inDtoGiftCertificate);
        //Then
        assertEquals(outDtoGiftCertificate, actualDtoGiftCertificate);
        verify(giftCertificateValidator, only()).validateCreateOrUpdate(inDtoGiftCertificate, HttpMethod.POST.name());
        verify(tagService, only()).prepareTagsForGiftCertificate(inGiftCertificate.getTags());
        verify(giftCertificateRepository, times(1)).findByName(inGiftCertificate.getName());
        verify(giftCertificateRepository, times(1)).save(inGiftCertificate);
        verify(mapper, times(1)).dtoToModel(inDtoGiftCertificate);
        verify(mapper, times(1)).modelToDto(outGiftCertificate);
        verifyNoMoreInteractions(giftCertificateRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowServiceException_On_Create() {
        //Given
        when(request.getMethod()).thenReturn(HttpMethod.POST.name());
        when(mapper.dtoToModel(inDtoGiftCertificate)).thenReturn(inGiftCertificate);
        when(giftCertificateRepository.findByName(inGiftCertificate.getName())).thenReturn(Optional.of(inGiftCertificate));
        when(inGiftCertificate.getActive()).thenReturn(true);
        final ServiceException expectedException = new ServiceException(
                rb.getString("giftCertificate.alreadyExists.name"),
                HttpStatus.CONFLICT, properties.getGift(), inGiftCertificate.getName());
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> giftCertificateService.create(inDtoGiftCertificate));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(giftCertificateValidator, only()).validateCreateOrUpdate(inDtoGiftCertificate, HttpMethod.POST.name());
        verify(giftCertificateRepository, times(1)).findByName(inGiftCertificate.getName());
        verify(mapper, only()).dtoToModel(inDtoGiftCertificate);
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
                .thenReturn(outGiftCertificates);
        when(mapper.modelsToDto(outGiftCertificates)).thenReturn(outDtoGiftCertificates);
        //When
        final Page<GiftCertificateDto> actualDtoGiftCertificates
                = giftCertificateService.readAll(Collections.emptyList(), container, page);
        //Then
        assertPages(outDtoGiftCertificates, actualDtoGiftCertificates);
        verify(giftCertificateValidator, only()).readAllValidate(Collections.emptyList(), container);
        verify(pageableValidator, only()).paginationValidate(page);
        verify(mapper, only()).modelsToDto(outGiftCertificates);
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
                .thenReturn(outGiftCertificates);
        when(mapper.modelsToDto(outGiftCertificates)).thenReturn(outDtoGiftCertificates);
        //When
        final Page<GiftCertificateDto> actualDtoGiftCertificates
                = giftCertificateService.readAll(Collections.emptyList(), container, page);
        //Then
        assertPages(outDtoGiftCertificates, actualDtoGiftCertificates);
        verify(giftCertificateValidator, only()).readAllValidate(Collections.emptyList(), container);
        verify(pageableValidator, only()).paginationValidate(page);
        verify(mapper, only()).modelsToDto(outGiftCertificates);
    }

    @Test
    void shouldReturnGiftCertificate_On_ReadOne_For_User() {
        //Given
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(giftCertificateRepository.findByIdAndActive(1, true))
                .thenReturn(Optional.of(outGiftCertificate));
        when(mapper.modelToDto(outGiftCertificate)).thenReturn(outDtoGiftCertificate);
        //When
        final GiftCertificateDto actualDtoGiftCertificate = giftCertificateService.readOne(1);
        //Then
        assertEquals(outDtoGiftCertificate, actualDtoGiftCertificate);
        verify(authorityValidator, only()).isAdmin();
        verify(giftCertificateRepository, only()).findByIdAndActive(1, true);
        verify(mapper, only()).modelToDto(outGiftCertificate);
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
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.of(outGiftCertificate));
        when(mapper.modelToDto(outGiftCertificate)).thenReturn(outDtoGiftCertificate);
        //When
        final GiftCertificateDto actualDtoGiftCertificate = giftCertificateService.readOne(1);
        //Then
        assertEquals(outDtoGiftCertificate, actualDtoGiftCertificate);
        verify(authorityValidator, only()).isAdmin();
        verify(giftCertificateRepository, only()).findById(1);
        verify(mapper, only()).modelToDto(outGiftCertificate);
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
        when(inGiftCertificate.getTags()).thenReturn(Collections.singleton(inputTag));
        when(giftCertificateRepository.findByName(inGiftCertificate.getName())).thenReturn(Optional.empty());
        when(request.getMethod()).thenReturn(HttpMethod.PATCH.name());
        when(mapper.dtoToModel(inDtoGiftCertificate)).thenReturn(inGiftCertificate);
        when(giftCertificateRepository.findById(inGiftCertificate.getId())).thenReturn(Optional.of(inGiftCertificate));
        when(giftCertificateRepository.save(inGiftCertificate)).thenReturn(outGiftCertificate);
        when(mapper.modelToDto(outGiftCertificate)).thenReturn(outDtoGiftCertificate);
        //When
        final GiftCertificateDto actualDtoGiftCertificate = giftCertificateService.update(inDtoGiftCertificate);
        //Then
        assertEquals(outDtoGiftCertificate, actualDtoGiftCertificate);
        verify(giftCertificateValidator, only()).validateCreateOrUpdate(inDtoGiftCertificate, HttpMethod.PATCH.name());
        verify(giftCertificateRepository, times(1)).findByName(inGiftCertificate.getName());
        verify(mapper, times(1)).dtoToModel(inDtoGiftCertificate);
        verify(giftCertificateRepository, times(1)).findById(inGiftCertificate.getId());
        verify(tagService, only()).prepareTagsForGiftCertificate(inGiftCertificate.getTags());
        verify(giftCertificateRepository, times(1)).save(inGiftCertificate);
        verify(mapper, times(1)).modelToDto(outGiftCertificate);
        verifyNoMoreInteractions(giftCertificateRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowServiceException_On_Update() {
        //Given
        when(request.getMethod()).thenReturn(HttpMethod.PATCH.name());
        when(giftCertificateRepository.findByName(inGiftCertificate.getName()))
                .thenReturn(Optional.of(inGiftCertificate));
        final ServiceException expectedException = new ServiceException(
                rb.getString("giftCertificate.alreadyExists.name"),
                HttpStatus.CONFLICT, properties.getGift(), inGiftCertificate.getName());
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> giftCertificateService.update(inDtoGiftCertificate));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(giftCertificateValidator, only()).validateCreateOrUpdate(inDtoGiftCertificate, HttpMethod.PATCH.name());
        verify(giftCertificateRepository, only()).findByName(inGiftCertificate.getName());
    }

    @Test
    void shouldDelete_On_Delete() {
        //Given
        when(giftCertificateRepository.findByIdAndActive(1, true)).thenReturn(Optional.of(inGiftCertificate));
        when(giftCertificateRepository.save(inGiftCertificate)).thenReturn(outGiftCertificate);
        when(mapper.modelToDto(outGiftCertificate)).thenReturn(outDtoGiftCertificate);
        //When
       final GiftCertificateDto actualDtoGiftCertificate = giftCertificateService.deleteById(1);
        //Then
        assertEquals(outDtoGiftCertificate, actualDtoGiftCertificate);
        verify(giftCertificateValidator, only()).validateId(1);
        verify(giftCertificateRepository, times(1)).findByIdAndActive(1, true);
        verify(giftCertificateRepository, times(1)).save(inGiftCertificate);
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
