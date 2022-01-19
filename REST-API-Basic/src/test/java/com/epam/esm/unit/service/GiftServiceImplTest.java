package com.epam.esm.unit.service;//package com.epam.esm.unit.service;
//
//import com.epam.esm.dao.GiftCertificateDao;
//import com.epam.esm.dao.GiftCertificateTagDao;
//import com.epam.esm.dao.TagDao;
//import com.epam.esm.model.GiftCertificate;
//import com.epam.esm.model.Tag;
//import com.epam.esm.service.AlreadyExistException;
//import com.epam.esm.service.GiftCertificateServiceImpl;
//import com.epam.esm.service.NotFoundException;
//import com.epam.esm.unit.service.config.TestConfig;
//import com.epam.esm.validator.CreateGiftCertificateValidator;
//import com.epam.esm.validator.ReadAllGiftCertificatesValidator;
//import com.epam.esm.validator.UpdateGiftCertificateValidator;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//
//import java.time.Clock;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;

import com.epam.esm.dao.*;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.AlreadyExistException;
import com.epam.esm.service.GiftCertificateServiceImpl;
import com.epam.esm.service.NotFoundException;
import com.epam.esm.validator.CreateGiftCertificateValidator;
import com.epam.esm.validator.ReadAllGiftCertificatesValidator;
import com.epam.esm.validator.UpdateGiftCertificateValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class GiftServiceImplTest {

    private final Clock clock = Mockito.mock(Clock.class);
    private final GiftCertificateDao giftCertificateDao = Mockito.mock(GiftCertificateDaoImpl.class);
    private final GiftCertificateTagDao giftCertificateTagDao = Mockito.mock(GiftCertificateTagDaoImpl.class);
    private final TagDao tagDao = Mockito.mock(TagDaoImpl.class);
    private final ReadAllGiftCertificatesValidator readAllGiftCertificatesValidator
            = Mockito.mock(ReadAllGiftCertificatesValidator.class);
    private final CreateGiftCertificateValidator createGiftCertificateValidator
            = Mockito.mock(CreateGiftCertificateValidator.class);
    private final UpdateGiftCertificateValidator updateGiftCertificateValidator
            = Mockito.mock(UpdateGiftCertificateValidator.class);

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    private final Tag oldTag = new Tag(1, "oldTag");
    private final Tag newTag = new Tag(1, "newTag");

    private final GiftCertificate giftCertificate = new GiftCertificate(1,
            "giftCertificate1",
            "description1",
            1.0f,
            1,
            null,
            null,
            new HashSet<>(Collections.singletonList(oldTag)));

    private final GiftCertificate giftCertificate2 = new GiftCertificate(2,
            "giftCertificate2",
            "description2",
            2.0f,
            2,
            null,
            null,
            new HashSet<>(Collections.singletonList(newTag)));

    private LocalDateTime localDateTimeMocked = LocalDateTime.of(2010, 1, 1, 1, 1, 1, 1);

    @Test
    void shouldCreateGiftCertificate_On_Create_WithOldTag() {

        try (MockedStatic mockedStatic = mockStatic(LocalDateTime.class)) {
            when(LocalDateTime.now(clock)).thenReturn(localDateTimeMocked);
            when(giftCertificateDao.readOneByName(giftCertificate.getName())).thenReturn(null);
            when(giftCertificateDao.create(giftCertificate)).thenReturn(giftCertificate.getId());

            when(tagDao.readOneByName(oldTag.getName())).thenReturn(oldTag);

            final GiftCertificate actual = giftCertificateService.create(giftCertificate);
            final GiftCertificate expected = giftCertificate;
            assertEquals(expected, actual);
        }
    }

    @Test
    void shouldCreateGiftCertificate_On_Create_WithNewTag() {
        try (MockedStatic mockedStatic = mockStatic(LocalDateTime.class)) {
            when(LocalDateTime.now(clock)).thenReturn(localDateTimeMocked);
            when(giftCertificateDao.readOneByName(giftCertificate.getName())).thenReturn(null);
            when(giftCertificateDao.create(giftCertificate)).thenReturn(giftCertificate.getId());

            when(tagDao.readOneByName(newTag.getName())).thenReturn(null);
            when(tagDao.create(newTag)).thenReturn(newTag.getId());

            final GiftCertificate actual = giftCertificateService.create(giftCertificate);
            final GiftCertificate expected = giftCertificate;
            assertEquals(expected, actual);
        }
    }

    @Test
    void shouldThrowAlreadyExistException_On_Create() {
        when(giftCertificateDao.readOneByName(giftCertificate.getName())).thenReturn(giftCertificate);
        final AlreadyExistException alreadyExistException =
                assertThrows(AlreadyExistException.class, () -> giftCertificateService.create(giftCertificate));
        assertEquals("Gift certificate with name = " + giftCertificate.getName() + " is already exist",
                alreadyExistException.getMessage());
    }

    @Test
    void shouldReturnGiftCertificates_On_ReadAll() {
        final List<GiftCertificate> giftCertificates = Arrays.asList(giftCertificate, giftCertificate2);
        when(giftCertificateDao.readAll(null, null, null, null)).thenReturn(giftCertificates);
        final List<GiftCertificate> actual = giftCertificateService.readAll(null, null, null, null);
        final List<GiftCertificate> expected = giftCertificates;
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnGiftCertificate_On_ReadOne() {
        when(giftCertificateDao.readOne(giftCertificate.getId())).thenReturn(giftCertificate);
        final GiftCertificate actual = giftCertificateService.readOne(giftCertificate.getId());
        final GiftCertificate expected = giftCertificate;
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateGiftCertificate_On_Create_WithOldTag() {

        try (MockedStatic mockedStatic = mockStatic(LocalDateTime.class)) {
            when(LocalDateTime.now(clock)).thenReturn(localDateTimeMocked);
            when(giftCertificateDao.readOne(giftCertificate.getId())).thenReturn(giftCertificate);
            when(giftCertificateDao.readOneByName(giftCertificate.getName())).thenReturn(null);
            doNothing().when(giftCertificateDao).update(giftCertificate);
            giftCertificateService.update(giftCertificate);
            verify(giftCertificateDao, times(1)).update(giftCertificate);
            when(tagDao.readOneByName(oldTag.getName())).thenReturn(oldTag);
        }
    }

    @Test
    void shouldDeleteTag_On_DeleteById() {
        Mockito.doReturn(giftCertificate).when(giftCertificateDao).readOne(giftCertificate.getId());
        doNothing().when(giftCertificateTagDao).deleteGiftCertificateTagByCertificateId(giftCertificate.getId());
        giftCertificateService.deleteById(giftCertificate.getId());
        verify(giftCertificateDao, times(1)).deleteById(giftCertificate.getId());
    }

    @Test
    void shouldThrowNotFoundException_On_DeleteById() {
        when(giftCertificateDao.readOne(giftCertificate.getId())).thenReturn(null);
        final NotFoundException notFoundException =
                assertThrows(NotFoundException.class, () -> giftCertificateService.deleteById(giftCertificate.getId()));
        assertEquals("Gift certificate with id = " + giftCertificate.getId() + " not found", notFoundException.getMessage());
    }

    @Test
    void shouldReturnGiftCertificates_On_ReadAllByTagName() {
        final List<GiftCertificate> giftCertificates = Collections.singletonList(giftCertificate);
        when(giftCertificateDao.readAll(oldTag.getName(), null, null, null)).thenReturn(giftCertificates);
        final List<GiftCertificate> actual = giftCertificateService.readAll(oldTag.getName(), null, null, null);
        final List<GiftCertificate> expected = giftCertificates;
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnGiftCertificates_On_ReadAllContainsName() {
        final List<GiftCertificate> giftCertificates = Collections.singletonList(giftCertificate);
        when(giftCertificateDao.readAll(null, giftCertificate.getName(), null, null)).thenReturn(giftCertificates);
        final List<GiftCertificate> actual = giftCertificateService.readAll(null, giftCertificate.getName(), null, null);
        final List<GiftCertificate> expected = giftCertificates;
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnGiftCertificates_On_ReadAllContainsDescription() {
        final List<GiftCertificate> giftCertificates = Collections.singletonList(giftCertificate);
        when(giftCertificateDao.readAll(null, null, giftCertificate.getDescription(), null)).thenReturn(giftCertificates);
        final List<GiftCertificate> actual = giftCertificateService.readAll(null, null, giftCertificate.getDescription(), null);
        final List<GiftCertificate> expected = giftCertificates;
        assertEquals(expected, actual);
    }

}