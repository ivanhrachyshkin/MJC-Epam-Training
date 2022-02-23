package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.PaginationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private DtoMapper<GiftCertificate, GiftCertificateDto> mapper;
    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private GiftCertificateValidator giftCertificateValidator;
    @Mock
    private PaginationValidator paginationValidator;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    private ResourceBundle rb;
    private GiftCertificate giftCertificate1;
    private GiftCertificateDto giftCertificateDto1;
    private Tag tag1;
    private Tag tag2;
    private TagDto dtoTag1;
    private TagDto dtoTag2;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(giftCertificateService, "rb", rb);

        giftCertificate1 = new GiftCertificate(1);
        tag1 = new Tag(1);
        tag2 = new Tag(2);
        giftCertificate1.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));

        giftCertificateDto1 = new GiftCertificateDto(1);
        dtoTag1 = new TagDto(1);
        dtoTag2 = new TagDto(2);
        giftCertificateDto1.setDtoTags(new HashSet<>(Arrays.asList(dtoTag1, dtoTag2)));
    }

    @Test
    void shouldThrowException_On_Create() {
        //Given
        final String message = String.format(
                rb.getString("giftCertificate.alreadyExists.name"), giftCertificate1.getName());
        when(giftCertificateRepository.readOneByName(giftCertificate1.getName()))
                .thenReturn(Optional.of(giftCertificate1));
        //When
        final ServiceException serviceException
                = assertThrows(ServiceException.class, () -> giftCertificateService.create(giftCertificateDto1));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(giftCertificateValidator, only()).createValidate(giftCertificateDto1);
        verify(giftCertificateRepository, only()).readOneByName(giftCertificate1.getName());
    }

    @Test
    void shouldCreateGiftCertificate_On_Create() {
        //Given
        when(giftCertificateRepository.readOneByName(giftCertificate1.getName())).thenReturn(Optional.empty());
        when(giftCertificateRepository.create(giftCertificate1)).thenReturn(giftCertificate1);
        when(mapper.dtoToModel(giftCertificateDto1)).thenReturn(giftCertificate1);
        when(mapper.modelToDto(giftCertificate1)).thenReturn(giftCertificateDto1);
        //When
        final GiftCertificateDto actualGiftCertificateDto = giftCertificateService.create(giftCertificateDto1);
        //Then
        assertEquals(giftCertificateDto1, actualGiftCertificateDto);
        verify(giftCertificateValidator, only()).createValidate(giftCertificateDto1);
        verify(giftCertificateRepository, times(1)).readOneByName(giftCertificate1.getName());
        verify(giftCertificateRepository, times(1)).create(giftCertificate1);
        verifyNoMoreInteractions(giftCertificateRepository);
        verify(mapper, times(1)).dtoToModel(giftCertificateDto1);
        verify(mapper, times(1)).modelToDto(giftCertificate1);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldReturnGiftCertificates_On_ReadAll() {
        //Given
        final GiftCertificateRequestParamsContainer container = new GiftCertificateRequestParamsContainer(
                "name",
                "description",
                "dateSort",
                "nameSort");
        final GiftCertificate giftCertificate2 = new GiftCertificate();
        giftCertificate2.setName("giftCertificate2");
        giftCertificate2.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));

        final GiftCertificateDto giftCertificateDto2 = new GiftCertificateDto();
        giftCertificateDto2.setName("giftCertificate2");
        giftCertificateDto2.setDtoTags(new HashSet<>(Arrays.asList(dtoTag1, dtoTag2)));

        final List<GiftCertificate> giftCertificates = Arrays.asList(giftCertificate1, giftCertificate2);
        final List<GiftCertificateDto> expectedGiftCertificates
                = Arrays.asList(giftCertificateDto1, giftCertificateDto2);
        //When
        when(giftCertificateRepository.readAll(Collections.singletonList(tag1.getName()),
                container.getName(),
                container.getDescription(),
                container.getDateSort(),
                container.getNameSort(),
                null,
                null))
                .thenReturn(giftCertificates);
        when(mapper.modelsToDto(giftCertificates)).thenReturn(expectedGiftCertificates);

        final List<GiftCertificateDto> actualGiftCertificates
                = giftCertificateService.readAll(
                Collections.singletonList(tag1.getName()),
                container,
                null,
                null);
        //Then
        assertEquals(expectedGiftCertificates, actualGiftCertificates);
        verify(giftCertificateValidator, only()).readAllValidate(
                Collections.singletonList(tag1.getName()), container);
        verify(giftCertificateRepository, only()).readAll(Collections.singletonList(tag1.getName()),
                container.getName(),
                container.getDescription(),
                container.getDateSort(),
                container.getNameSort(),
                null,
                null);
        verify(paginationValidator, only()).paginationValidate(null, null);
    }

    @Test
    void shouldReturnGiftCertificate_On_ReadOne() {
        //Given
        when(giftCertificateRepository.readOne(giftCertificate1.getId())).thenReturn(Optional.of(giftCertificate1));
        when(mapper.modelToDto(giftCertificate1)).thenReturn(giftCertificateDto1);
        //When
        final GiftCertificateDto actual = giftCertificateService.readOne(giftCertificate1.getId());
        //Then
        assertEquals(giftCertificateDto1, actual);
        verify(giftCertificateRepository, only()).readOne(giftCertificate1.getId());
    }

    @Test
    void shouldThrowException_On_ReadOne() {
        //Given
        final String message = String.format(rb.getString("giftCertificate.notFound.id"), giftCertificate1.getId());
        when(giftCertificateRepository.readOne(giftCertificate1.getId())).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException
                = assertThrows(
                        ServiceException.class, () -> giftCertificateService.readOne(giftCertificate1.getId()));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(giftCertificateRepository, only()).readOne(giftCertificate1.getId());
    }

    @Test
    void shouldDeleteTag_On_DeleteById() {
        //Given
        when(giftCertificateRepository.deleteById(giftCertificate1.getId())).thenReturn(Optional.of(giftCertificate1));
        //When
        giftCertificateService.deleteById(giftCertificate1.getId());
        //Then
        verify(giftCertificateRepository, only()).deleteById(giftCertificate1.getId());
    }

    @Test
    void shouldThrowException_On_DeleteById() {
        //Given
        final String message = String.format(rb.getString("giftCertificate.notFound.id"), giftCertificate1.getId());
        when(giftCertificateRepository.deleteById(giftCertificate1.getId())).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException
                = assertThrows(
                        ServiceException.class, () -> giftCertificateService.deleteById(giftCertificate1.getId()));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(giftCertificateRepository, only()).deleteById(giftCertificate1.getId());
    }

    @Test
    void shouldUpdateGiftCertificate_On_Update_WithTags() {
        //Given
        when(giftCertificateRepository.readOne(giftCertificate1.getId())).thenReturn(Optional.of(giftCertificate1));
        when(giftCertificateRepository.readOneByName(giftCertificate1.getName())).thenReturn(Optional.empty());
        when(giftCertificateRepository.update(giftCertificate1)).thenReturn(giftCertificate1);
        when(mapper.dtoToModel(giftCertificateDto1)).thenReturn(giftCertificate1);
        when(mapper.modelToDto(giftCertificate1)).thenReturn(giftCertificateDto1);
        //When
        giftCertificateService.update(giftCertificateDto1);
        //Then
        verify(giftCertificateValidator, only()).updateValidate(giftCertificateDto1);
        verify(giftCertificateRepository, times(1)).readOne(giftCertificate1.getId());
        verify(giftCertificateRepository, times(1)).readOneByName(giftCertificate1.getName());
        verify(giftCertificateRepository, times(1)).update(giftCertificate1);
        verifyNoMoreInteractions(giftCertificateRepository);
        verify(mapper, times(1)).dtoToModel(giftCertificateDto1);
        verify(mapper, times(1)).modelToDto(giftCertificate1);
        verifyNoMoreInteractions(mapper);
    }
}