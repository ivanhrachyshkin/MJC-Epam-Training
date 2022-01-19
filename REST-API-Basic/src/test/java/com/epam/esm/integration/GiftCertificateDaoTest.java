package com.epam.esm.integration;

import com.epam.esm.config.ApplicationConfig;
import com.epam.esm.dao.*;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Sql("/create-tables.sql")
@Sql("/insert-data-tables.sql")
@ActiveProfiles("test")
@WebAppConfiguration
public class GiftCertificateDaoTest {

    @Autowired
    private GiftCertificateDao giftCertificateDao;
    @Autowired
    private GiftCertificateTagDao giftCertificateTagDao;
    @Autowired
    private TagDao tagDao;

    final Tag tag1 = new Tag(1, "firstTag");
    final Tag tag2 = new Tag(2, "secondTag");

    final HashSet<Tag> tags1 = new HashSet<>(Arrays.asList(tag1, tag2));
    final HashSet<Tag> tags2 = new HashSet<>(Collections.singletonList(tag1));

    final GiftCertificate oldGiftCertificateWithTags1 = new GiftCertificate(1,
            "g1",
            "d1",
            1.0F,
            1,
            Timestamp.valueOf("2010-01-01 01:01:01.111").toLocalDateTime(),
            Timestamp.valueOf("2010-01-01 01:01:01.111").toLocalDateTime(),
            tags1);

    final GiftCertificate oldGiftCertificateWithTags2 = new GiftCertificate(2,
            "g2",
            "d2",
            2.0F,
            2,
            Timestamp.valueOf("2020-02-02 02:02:02.222").toLocalDateTime(),
            Timestamp.valueOf("2020-02-02 02:02:02.222").toLocalDateTime(),
            tags2);

    final List<GiftCertificate> giftCertificates
            = Arrays.asList(oldGiftCertificateWithTags1, oldGiftCertificateWithTags2);

    @Test
    void shouldReturnGiftCertificateWithTags_On_ReadOneByName() {
        final GiftCertificate newGiftCertificateWithTags = giftCertificateDao.readOneByName("g1");
        Assertions.assertEquals(oldGiftCertificateWithTags1, newGiftCertificateWithTags);
    }

    @Test
    void shouldReturnNull_On_ReadOneByaName_NotExists() {
        final GiftCertificate newGiftCertificateWithTags = giftCertificateDao.readOneByName("aaa");
        Assertions.assertNull(newGiftCertificateWithTags);
    }

    @Test
    void shouldReturnGiftCertificateWithTags_On_ReadOne() {
        final GiftCertificate newGiftCertificateWithTags = giftCertificateDao.readOne(1);
        Assertions.assertEquals(oldGiftCertificateWithTags1, newGiftCertificateWithTags);
    }

    @Test
    void shouldReturnNull_On_ReadOne_NotExist() {
        final GiftCertificate newGiftCertificateWithTags = giftCertificateDao.readOne(1000);
        Assertions.assertNull(newGiftCertificateWithTags);
    }

    @Test
    void shouldDeleteGiftCertificateById_On_DeleteById() {
        Assertions.assertNotNull(giftCertificateDao.readOne(1));
        giftCertificateTagDao.deleteGiftCertificateTagByCertificateId(1);
        giftCertificateDao.deleteById(1);
        Assertions.assertNull(giftCertificateDao.readOne(1));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_Read() {

        Assertions.assertEquals(giftCertificates,
                giftCertificateDao.readAll(null, null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTag_Positive() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificateWithTags1),
                giftCertificateDao.readAll("secondTag", null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTag_Negative() {
        Assertions.assertEquals(Collections.emptyList(), giftCertificateDao.readAll("secondTags", null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadName() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificateWithTags1),
                giftCertificateDao.readAll(null, "g1", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadName_Negative() {
        Assertions.assertEquals(Collections.emptyList(), giftCertificateDao.readAll(null, "asd", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadDescription_Positive() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificateWithTags2),
                giftCertificateDao.readAll(null, null, "d2", null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadDescription_Negative() {
        Assertions.assertEquals(Collections.emptyList(), giftCertificateDao.readAll(null, null, "fg", null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadSort_ASC() {
        Assertions.assertEquals(giftCertificates,
                giftCertificateDao.readAll(null, null, null, true));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadSort_DESC() {
        Assertions.assertNotEquals(giftCertificates,
                giftCertificateDao.readAll(null, null, null, false));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndName_Positive() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificateWithTags1),
                giftCertificateDao.readAll("secondTag", "1", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndName_Negative() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao.readAll("secondTag", "2", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndName_Negative_ForTag() {
        Assertions.assertEquals(Collections.emptyList(), giftCertificateDao.readAll("secondTag2", "g1", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndName_Negative_ForName() {
        Assertions.assertEquals(Collections.emptyList(), giftCertificateDao.readAll("secondTag", "fgd", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndName_Negative_ForTagAndName() {
        Assertions.assertEquals(Collections.emptyList(), giftCertificateDao.readAll("secondTaga", "fgd", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndDescription_Positive() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificateWithTags2),
                giftCertificateDao.readAll("firstTag", null, "2", null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndDescription_Negative() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao.readAll("secondTag", null, "2", null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndDescription_Negative_ForTag() {
        Assertions.assertEquals(Collections.emptyList(), giftCertificateDao.readAll("secondTag2a", null, "1", null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndDescription_Negative_ForDescription() {
        Assertions.assertEquals(Collections.emptyList(), giftCertificateDao.readAll("secondTag", null, "dfg", null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndDescription_Negative_ForTagAndDescription() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao.readAll("secondTaga", null, "asd", null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadNameAndDescription_Positive() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificateWithTags1),
                giftCertificateDao.readAll(null, "1", "1", null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadNameAndDescription_Negative() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao.readAll(null, "1", "2", true));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadNameAndDescription_Negative_ForName() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao.readAll(null, "eh", "2", null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadNameAndDescription_Negative_ForDescription() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao.readAll(null, "1", "lkjh", null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadNameAndDescription_Negative_ForNameAndDescription() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao.readAll(null, "asd", "asd", null));
    }


}
