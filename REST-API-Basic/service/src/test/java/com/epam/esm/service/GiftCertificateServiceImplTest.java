package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.validator.CreateGiftCertificateValidator;
import com.epam.esm.service.validator.ReadAllGiftCertificatesValidator;
import com.epam.esm.service.validator.UpdateGiftCertificateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateDao giftCertificateDao;
    @Mock
    private GiftCertificateTagDao giftCertificateTagDao;
    @Mock
    private TagDao tagDao;
    @Mock
    private ReadAllGiftCertificatesValidator readAllGiftCertificatesValidator;
    @Mock
    private CreateGiftCertificateValidator createGiftCertificateValidator;
    @Mock
    private UpdateGiftCertificateValidator updateGiftCertificateValidator;
    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    private GiftCertificate giftCertificate1;
    private GiftCertificate giftCertificate2;
    private Tag tag1;
    private Tag tag2;

    @BeforeEach
    public void setUp() {
        giftCertificate1 = new GiftCertificate();
        tag1 = new Tag(1,"tag1");
        tag2 = new Tag(2,"tag2");
        giftCertificate1.setId(1);
        giftCertificate1.setName("giftCertificate1");
        giftCertificate1.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));
    }

    @Test
    void shouldCreateGiftCertificate_On_Create_WithTags() {
        //Given
        when(giftCertificateDao.readOneByName(giftCertificate1.getName())).thenReturn(Optional.empty());
        when(giftCertificateDao.create(giftCertificate1)).thenReturn(giftCertificate1);
        when(tagDao.readOneByName(tag1.getName())).thenReturn(Optional.empty());
        when(tagDao.readOneByName(tag2.getName())).thenReturn(Optional.of(tag2));
        when(tagDao.create(tag1)).thenReturn(tag1);

        //When
        final GiftCertificate actualGiftCertificate = giftCertificateService.create(giftCertificate1);

        //Then
        assertEquals(giftCertificate1, actualGiftCertificate);

        verify(createGiftCertificateValidator, only()).validate(giftCertificate1);
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
    }

    @Test
    void shouldThrowException_On_Create_WithTags() {
        //Given
        when(giftCertificateDao
                .readOneByName(giftCertificate1.getName())).thenReturn(Optional.of(giftCertificate1));
        //When
        final ServiceException serviceException
                = assertThrows(ServiceException.class, () -> giftCertificateService.create(giftCertificate1));
        //Then
        assertEquals("Gift certificate with name = " + giftCertificate1.getName() + " is already exist",
                serviceException.getMessage());
        verify(createGiftCertificateValidator, only()).validate(giftCertificate1);
        verify(giftCertificateDao, only()).readOneByName(giftCertificate1.getName());
    }

    @Test
    void shouldReturnGiftCertificates_On_ReadAll() {
        //Given
        giftCertificate2 = new GiftCertificate();
        giftCertificate2.setName("giftCertificate2");
        giftCertificate2.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));
        final List<GiftCertificate> expectedGiftCertificates
                = Arrays.asList(giftCertificate1, giftCertificate2);
        //When
        when(giftCertificateDao.readAll(null, null, null, null))
                .thenReturn(expectedGiftCertificates);
        final List<GiftCertificate> actualGiftCertificates
                = giftCertificateService.readAll(null, null, null, null);
        //Then
        assertEquals(expectedGiftCertificates, actualGiftCertificates);
        verify(readAllGiftCertificatesValidator, only()).validate(null, null, null);
        verify(giftCertificateDao, only()).readAll(null, null, null, null);
    }

    @Test
    void shouldReturnGiftCertificate_On_ReadOne() {
        //When
        when(giftCertificateDao
                .readOne(giftCertificate1.getId())).thenReturn(Optional.of(giftCertificate1));
        final GiftCertificate actual = giftCertificateService.readOne(giftCertificate1.getId());
        //Then
        assertEquals(giftCertificate1, actual);
        verify(giftCertificateDao, only()).readOne(giftCertificate1.getId());
    }

    @Test
    void shouldThrowException_On_ReadOne() {
        //When
        when(giftCertificateDao
                .readOne(giftCertificate1.getId())).thenReturn(Optional.empty());
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> giftCertificateService.readOne(giftCertificate1.getId()));
        //Then
        assertEquals("Gift certificate with id = " + giftCertificate1.getId() + " not found",
                serviceException.getMessage());
        verify(giftCertificateDao, only()).readOne(giftCertificate1.getId());
    }

    @Test
    void shouldDeleteTag_On_DeleteById() {
        //Given
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        doNothing().when(giftCertificateTagDao).deleteGiftCertificateTagByCertificateId(giftCertificate.getId());
        doNothing().when(giftCertificateDao).deleteById(giftCertificate.getId());
        when(giftCertificateDao
                .readOne(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        //When
        giftCertificateService.deleteById(giftCertificate.getId());
        //Then
        verify(giftCertificateDao, times(1)).readOne(giftCertificate.getId());
        verify(giftCertificateDao, times(1)).deleteById(giftCertificate.getId());
        verifyNoMoreInteractions(giftCertificateDao);
        verify(giftCertificateTagDao, only())
                .deleteGiftCertificateTagByCertificateId(giftCertificate.getId());

    }

    @Test
    void shouldUpdateGiftCertificate_On_Update_WithTags() {
        //Given
        when(giftCertificateDao.readOne(giftCertificate1.getId())).thenReturn(Optional.of(giftCertificate1));
        when(giftCertificateDao.readOneByName(giftCertificate1.getName())).thenReturn(Optional.empty());
        when(tagDao.readOneByName(tag1.getName())).thenReturn(Optional.empty());
        when(tagDao.readOneByName(tag2.getName())).thenReturn(Optional.of(tag2));
        when(tagDao.create(tag1)).thenReturn(tag1);

        //When
        giftCertificateService.update(giftCertificate1);

        //Then
        verify(updateGiftCertificateValidator, only()).validate(giftCertificate1);
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
    }
}
