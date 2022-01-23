package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.validator.CreateGiftCertificateValidator;
import com.epam.esm.service.validator.ReadAllGiftCertificatesValidator;
import com.epam.esm.service.validator.UpdateGiftCertificateValidator;
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
class GiftServiceImplTest {

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

    @Test
    void shouldCreateGiftCertificate_On_Create_WithTags() {
        //Given
        final Tag creatingTag1 = new Tag("tag1");
        final Tag creatingTag2 = new Tag("tag2");
        final HashSet<Tag> creatingTags = new HashSet<>(Arrays.asList(creatingTag1, creatingTag2));
        final GiftCertificate creatingGiftCertificate = new GiftCertificate();
        creatingGiftCertificate.setName("creatingGiftCertificate");
        creatingGiftCertificate.setTags(creatingTags);

        final Tag createdTag1 = new Tag(1, "tag1");
        final Tag createdTag2 = new Tag(2, "tag2");
        final GiftCertificate createdGiftCertificate = new GiftCertificate();
        createdGiftCertificate.setId(1);
        createdGiftCertificate.setName("creatingGiftCertificate");
        createdGiftCertificate.setTags(creatingTags);

        final Tag expectedTag1 = new Tag(1, "tag1");
        final Tag expectedTag2 = new Tag(2, "tag2");
        final HashSet<Tag> expectedTags = new HashSet<>(Arrays.asList(expectedTag1, expectedTag2));
        final GiftCertificate expectedGiftCertificate = new GiftCertificate();
        expectedGiftCertificate.setId(1);
        expectedGiftCertificate.setName("creatingGiftCertificate");
        expectedGiftCertificate.setTags(expectedTags);

        when(giftCertificateDao.readOneByName(creatingGiftCertificate.getName())).thenReturn(Optional.empty());
        when(giftCertificateDao.create(creatingGiftCertificate)).thenReturn(createdGiftCertificate);
        when(tagDao.readOneByName(creatingTag1.getName())).thenReturn(Optional.empty());
        when(tagDao.readOneByName(creatingTag2.getName())).thenReturn(Optional.of(createdTag2));
        when(tagDao.create(creatingTag1)).thenReturn(createdTag1);

        //When
        final GiftCertificate actualGiftCertificate = giftCertificateService.create(creatingGiftCertificate);

        //Then
        assertEquals(expectedGiftCertificate, actualGiftCertificate);

        verify(createGiftCertificateValidator, only()).validate(creatingGiftCertificate);
        verify(giftCertificateDao, times(1)).readOneByName(creatingGiftCertificate.getName());
        verify(giftCertificateDao, times(1)).create(creatingGiftCertificate);
        verifyNoMoreInteractions(giftCertificateDao);
        verify(tagDao, times(1)).readOneByName(creatingTag1.getName());
        verify(tagDao, times(1)).readOneByName(creatingTag2.getName());
        verify(tagDao, times(1)).create(creatingTag1);
        verifyNoMoreInteractions(tagDao);
        verify(giftCertificateTagDao, times(1))
                .createGiftCertificateTag(createdGiftCertificate.getId(), createdTag1.getId());
        verify(giftCertificateTagDao, times(1))
                .createGiftCertificateTag(createdGiftCertificate.getId(), createdTag2.getId());
        verifyNoMoreInteractions(giftCertificateTagDao);
    }

    @Test
    void shouldThrowException_On_Create_WithTags() {

        //Given
        final GiftCertificate creatingGiftCertificate = new GiftCertificate();
        creatingGiftCertificate.setName("creatingGiftCertificate");
        final GiftCertificate existingGiftCertificate = new GiftCertificate();
        creatingGiftCertificate.setId(1);
        creatingGiftCertificate.setName("creatingGiftCertificate");
        when(giftCertificateDao
                .readOneByName(creatingGiftCertificate.getName())).thenReturn(Optional.of(existingGiftCertificate));
        //When
        final ServiceException serviceException
                = assertThrows(ServiceException.class, () -> giftCertificateService.create(creatingGiftCertificate));

        //Then
        assertEquals("Gift certificate with name = " + creatingGiftCertificate.getName() + " is already exist",
                serviceException.getMessage());
        verify(createGiftCertificateValidator, only()).validate(creatingGiftCertificate);
        verify(giftCertificateDao, only()).readOneByName(creatingGiftCertificate.getName());
    }

    @Test
    void shouldReturnGiftCertificates_On_ReadAll() {
        //Given
        final GiftCertificate expectedGiftCertificate1 = new GiftCertificate();
        final GiftCertificate expectedGiftCertificate2 = new GiftCertificate();
        final GiftCertificate expectedGiftCertificate3 = new GiftCertificate();
        final List<GiftCertificate> expectedGiftCertificates
                = Arrays.asList(expectedGiftCertificate1, expectedGiftCertificate2, expectedGiftCertificate3);
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
        //Given
        final GiftCertificate expectedGiftCertificate = new GiftCertificate();
        expectedGiftCertificate.setId(1);
        //When
        when(giftCertificateDao
                .readOne(expectedGiftCertificate.getId())).thenReturn(Optional.of(expectedGiftCertificate));
        final GiftCertificate actual = giftCertificateService.readOne(expectedGiftCertificate.getId());
        //Then
        assertEquals(expectedGiftCertificate, actual);
        verify(giftCertificateDao, only()).readOne(expectedGiftCertificate.getId());
    }

    @Test
    void shouldThrowException_On_ReadOne() {
        //Given
        final GiftCertificate expectedGiftCertificate = new GiftCertificate();
        expectedGiftCertificate.setId(1);
        //When
        when(giftCertificateDao
                .readOne(expectedGiftCertificate.getId())).thenReturn(Optional.empty());
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> giftCertificateService.readOne(expectedGiftCertificate.getId()));
        //Then
        assertEquals("Gift certificate with id = " + expectedGiftCertificate.getId() + " not found",
                serviceException.getMessage());
        verify(giftCertificateDao, only()).readOne(expectedGiftCertificate.getId());
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
        final Tag tag1 = new Tag(1,"tag1");
        final Tag tag2 = new Tag(2,"tag2");
        final HashSet<Tag> tags = new HashSet<>(Arrays.asList(tag1, tag2));
        final GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        giftCertificate.setName("creatingGiftCertificate");
        giftCertificate.setTags(tags);

        when(giftCertificateDao.readOne(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateDao.readOneByName(giftCertificate.getName())).thenReturn(Optional.empty());
        when(tagDao.readOneByName(tag1.getName())).thenReturn(Optional.empty());
        when(tagDao.readOneByName(tag2.getName())).thenReturn(Optional.of(tag2));
        when(tagDao.create(tag1)).thenReturn(tag1);

        //When
        giftCertificateService.update(giftCertificate);

        //Then
        verify(updateGiftCertificateValidator, only()).validate(giftCertificate);
        verify(giftCertificateDao, times(1)).readOne(giftCertificate.getId());
        verify(giftCertificateDao, times(1)).readOneByName(giftCertificate.getName());
        verify(giftCertificateDao, times(1)).update(giftCertificate);
        verifyNoMoreInteractions(giftCertificateDao);
        verify(tagDao, times(1)).readOneByName(tag1.getName());
        verify(tagDao, times(1)).readOneByName(tag2.getName());
        verify(tagDao, times(1)).create(tag1);
        verifyNoMoreInteractions(tagDao);
        verify(giftCertificateTagDao, times(1))
                .createGiftCertificateTag(giftCertificate.getId(), tag1.getId());
        verify(giftCertificateTagDao, times(1))
                .createGiftCertificateTag(giftCertificate.getId(), tag2.getId());
        verifyNoMoreInteractions(giftCertificateTagDao);
    }
}
