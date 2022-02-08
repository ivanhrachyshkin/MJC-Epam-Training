package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.GiftCertificateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    private final DummyRb dummyRb = new DummyRb();
    @Mock
    private DtoMapper<GiftCertificate, GiftCertificateDto> mapper;
    @Mock
    private GiftCertificateDao giftCertificateDao;
    @Mock
    private GiftCertificateTagDao giftCertificateTagDao;
    @Mock
    private TagDao tagDao;
    @Mock
    private GiftCertificateValidator giftCertificateValidator;
    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    private GiftCertificate giftCertificate1;
    private GiftCertificateDto giftCertificateDto1;
    private Tag tag1;
    private Tag tag2;
    private TagDto dtoTag1;
    private TagDto dtoTag2;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(giftCertificateService, "rb", dummyRb);
        giftCertificate1 = new GiftCertificate();
        tag1 = new Tag(1, "tag1");
        tag2 = new Tag(2, "tag2");
        giftCertificate1.setId(1);
        giftCertificate1.setName("giftCertificate1");
        giftCertificate1.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));

        giftCertificateDto1 = new GiftCertificateDto();
        dtoTag1 = new TagDto(1, "tag1");
        dtoTag2 = new TagDto(2, "tag2");
        giftCertificateDto1.setId(1);
        giftCertificateDto1.setName("giftCertificate1");
        giftCertificateDto1.setTags(new HashSet<>(Arrays.asList(dtoTag1, dtoTag2)));
    }

    @Test
    void shouldCreateGiftCertificate_On_Create_WithTags() {
        //Given
        when(giftCertificateDao.readOneByName(giftCertificate1.getName())).thenReturn(Optional.empty());
        when(giftCertificateDao.create(giftCertificate1)).thenReturn(giftCertificate1);
        when(tagDao.readOneByName(tag1.getName())).thenReturn(Optional.empty());
        when(tagDao.readOneByName(tag2.getName())).thenReturn(Optional.of(tag2));
        when(tagDao.create(tag1)).thenReturn(tag1);
        when(mapper.dtoToModel(giftCertificateDto1)).thenReturn(giftCertificate1);
        when(mapper.modelToDto(giftCertificate1)).thenReturn(giftCertificateDto1);

        //When
        final GiftCertificateDto actualGiftCertificateDto = giftCertificateService.create(giftCertificateDto1);

        //Then
        assertEquals(giftCertificateDto1, actualGiftCertificateDto);

        verify(giftCertificateValidator, only()).createValidate(giftCertificateDto1);
        verify(giftCertificateDao, times(1)).readOneByName(giftCertificate1.getName());
        verify(giftCertificateDao, times(1)).create(giftCertificate1);
        verifyNoMoreInteractions(giftCertificateDao);
        verify(tagDao, times(1)).readOneByName(tag1.getName());
        verify(tagDao, times(1)).readOneByName(tag2.getName());
        verify(tagDao, times(1)).create(tag1);
        verifyNoMoreInteractions(tagDao);
        verify(giftCertificateTagDao, times(1))
                .createGiftCertificateTag(giftCertificate1.getId(), tag1.getId());
        verify(giftCertificateTagDao, times(1))
                .createGiftCertificateTag(giftCertificate1.getId(), tag1.getId());
        verify(mapper, times(1)).dtoToModel(giftCertificateDto1);
        verify(mapper, times(1)).modelToDto(giftCertificate1);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowException_On_Create_WithTags() {
        //Given
        dummyRb.setMessage("giftCertificate.alreadyExists.name","Gift certificate with name = %s is already exist");
        final String message = String.format("Gift certificate with name = %s is already exist", giftCertificate1.getName());
        when(giftCertificateDao.readOneByName(giftCertificate1.getName())).thenReturn(Optional.of(giftCertificate1));
        when(mapper.dtoToModel(giftCertificateDto1)).thenReturn(giftCertificate1);
        //When
        final ServiceException serviceException
                = assertThrows(ServiceException.class, () -> giftCertificateService.create(giftCertificateDto1));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(giftCertificateValidator, only()).createValidate(giftCertificateDto1);
        verify(giftCertificateDao, only()).readOneByName(giftCertificate1.getName());
        verify(mapper, only()).dtoToModel(giftCertificateDto1);
    }

    @Test
    void shouldReturnGiftCertificates_On_ReadAll() {
        //Given
        final GiftCertificate giftCertificate2 = new GiftCertificate();
        giftCertificate2.setName("giftCertificate2");
        giftCertificate2.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));

        final GiftCertificateDto giftCertificateDto2 = new GiftCertificateDto();
        giftCertificateDto2.setName("giftCertificate2");
        giftCertificateDto2.setTags(new HashSet<>(Arrays.asList(dtoTag1, dtoTag2)));

        final List<GiftCertificate> giftCertificates = Arrays.asList(giftCertificate1, giftCertificate2);
        final List<GiftCertificateDto> expectedGiftCertificates
                = Arrays.asList(giftCertificateDto1, giftCertificateDto2);
        //When
        when(giftCertificateDao.readAll(null, null, null, null, null))
                .thenReturn(giftCertificates);
        when(mapper.modelsToDto(giftCertificates)).thenReturn(expectedGiftCertificates);

        final List<GiftCertificateDto> actualGiftCertificates
                = giftCertificateService.readAll(null, null, null, null, null);
        //Then
        assertEquals(expectedGiftCertificates, actualGiftCertificates);
        verify(giftCertificateValidator, only()).readAllValidate(null, null, null);
        verify(giftCertificateDao, only()).readAll(null, null, null, null, null);
    }

    @Test
    void shouldReturnGiftCertificate_On_ReadOne() {
        //When
        when(giftCertificateDao.readOne(giftCertificate1.getId())).thenReturn(Optional.of(giftCertificate1));
        when(mapper.modelToDto(giftCertificate1)).thenReturn(giftCertificateDto1);
        final GiftCertificateDto actual = giftCertificateService.readOne(giftCertificate1.getId());
        //Then
        assertEquals(giftCertificateDto1, actual);
        verify(giftCertificateDao, only()).readOne(giftCertificate1.getId());
    }

    @Test
    void shouldThrowException_On_ReadOne() {
        //Given
        dummyRb.setMessage("giftCertificate.notFound.id","Gift certificate with id = %s not found");
        when(giftCertificateDao.readOne(giftCertificate1.getId())).thenReturn(Optional.empty());
        final String message = String.format("Gift certificate with id = %s not found", giftCertificate1.getId());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> giftCertificateService.readOne(giftCertificate1.getId()));
        //Then

        assertEquals(message, serviceException.getMessage());
        verify(giftCertificateDao, only()).readOne(giftCertificate1.getId());
    }

    @Test
    void shouldDeleteTag_On_DeleteById() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        doNothing().when(giftCertificateTagDao).deleteGiftCertificateTagByCertificateId(giftCertificate.getId());
        doNothing().when(giftCertificateDao).deleteById(giftCertificate.getId());
        when(giftCertificateDao.readOne(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        //When
        giftCertificateService.deleteById(giftCertificate.getId());
        //Then
        verify(giftCertificateDao, times(1)).readOne(giftCertificate.getId());
        verify(giftCertificateDao, times(1)).deleteById(giftCertificate.getId());
        verifyNoMoreInteractions(giftCertificateDao);
        verify(giftCertificateTagDao, only()).deleteGiftCertificateTagByCertificateId(giftCertificate.getId());

    }

    @Test
    void shouldUpdateGiftCertificate_On_Update_WithTags() {
        //Given
        when(giftCertificateDao.readOne(giftCertificate1.getId())).thenReturn(Optional.of(giftCertificate1));
        when(giftCertificateDao.readOneByName(giftCertificate1.getName())).thenReturn(Optional.empty());
        when(tagDao.readOneByName(tag1.getName())).thenReturn(Optional.empty());
        when(tagDao.readOneByName(tag2.getName())).thenReturn(Optional.of(tag2));
        when(tagDao.create(tag1)).thenReturn(tag1);
        when(mapper.dtoToModel(giftCertificateDto1)).thenReturn(giftCertificate1);
        when(mapper.modelToDto(giftCertificate1)).thenReturn(giftCertificateDto1);

        //When
        giftCertificateService.update(giftCertificateDto1);

        //Then
        verify(giftCertificateValidator, only()).updateValidate(giftCertificateDto1);
        verify(giftCertificateDao, times(1)).readOne(giftCertificate1.getId());
        verify(giftCertificateDao, times(1)).readOneByName(giftCertificate1.getName());
        verify(giftCertificateDao, times(1)).update(giftCertificate1);
        verifyNoMoreInteractions(giftCertificateDao);
        verify(tagDao, times(1)).readOneByName(tag1.getName());
        verify(tagDao, times(1)).readOneByName(tag2.getName());
        verify(tagDao, times(1)).create(tag1);
        verifyNoMoreInteractions(tagDao);
        verify(giftCertificateTagDao, times(1))
                .createGiftCertificateTag(giftCertificate1.getId(), tag1.getId());
        verify(giftCertificateTagDao, times(1))
                .createGiftCertificateTag(giftCertificate1.getId(), tag2.getId());
        verifyNoMoreInteractions(giftCertificateTagDao);
        verify(mapper, times(1)).dtoToModel(giftCertificateDto1);
        verify(mapper, times(1)).modelToDto(giftCertificate1);
        verifyNoMoreInteractions(mapper);
    }
}
