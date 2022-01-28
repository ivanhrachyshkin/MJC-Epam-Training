package com.epam.esm.dao;

import com.epam.esm.dao.config.DaoConfig;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoConfig.class)
@Sql("/create-tables.sql")
@Sql("/insert-data-tables.sql")
@TestPropertySource("classpath:application-test.properties")
public class GiftCertificateDaoTest {

    @Autowired
    private GiftCertificateDao giftCertificateDao;
    @Autowired
    private GiftCertificateTagDao giftCertificateTagDao;

    final Tag oldTag1 = new Tag(1, "firstTag");
    final Tag oldTag2 = new Tag(2, "secondTag");

    final HashSet<Tag> tags1 = new HashSet<>(Arrays.asList(oldTag1, oldTag2));
    final HashSet<Tag> tags2 = new HashSet<>(Collections.singletonList(oldTag1));

    final GiftCertificate oldGiftCertificate1 = new GiftCertificate(1,
            "g1",
            "d1",
            1.0F,
            1,
            Timestamp.valueOf("2010-01-01 01:01:01.111").toLocalDateTime(),
            Timestamp.valueOf("2010-01-01 01:01:01.111").toLocalDateTime(),
            tags1);

    final GiftCertificate oldGiftCertificate2 = new GiftCertificate(2,
            "g2",
            "d2",
            2.0F,
            2,
            Timestamp.valueOf("2020-02-02 02:02:02.222").toLocalDateTime(),
            Timestamp.valueOf("2020-02-02 02:02:02.222").toLocalDateTime(),
            tags2);

    final GiftCertificate oldGiftCertificate3 = new GiftCertificate(
            "g3",
            "d3",
            3.0F,
            3,
            Timestamp.valueOf("2030-03-03 03:03:03.333").toLocalDateTime(),
            Timestamp.valueOf("2030-03-03 03:03:03.333").toLocalDateTime(),
            tags2);

    final List<GiftCertificate> oldGiftCertificates = Arrays.asList(oldGiftCertificate1, oldGiftCertificate2);

    @Test
    void shouldReturnGiftCertificateWithTags_On_ReadOneByName() {
        final Optional<GiftCertificate> newGiftCertificateWithTags = giftCertificateDao.readOneByName("g1");
        Assertions.assertTrue(newGiftCertificateWithTags.isPresent());
        Assertions.assertEquals(oldGiftCertificate1, newGiftCertificateWithTags.get());
    }

    @Test
    void shouldReturnFalse_On_ReadOneByName_NotExists() {
        boolean result = giftCertificateDao.readOneByName("aaa").isPresent();
        Assertions.assertFalse(result);
    }

    @Test
    void shouldReturnGiftCertificateWithTags_On_ReadOne() {
        final Optional<GiftCertificate> giftCertificate = giftCertificateDao.readOne(1);
        Assertions.assertTrue(giftCertificate.isPresent());
        Assertions.assertEquals(oldGiftCertificate1, giftCertificate.get());
    }

    @Test
    void shouldReturnNull_On_ReadOne_NotExist() {
        boolean result = giftCertificateDao.readOne(1000).isPresent();
        Assertions.assertFalse(result);
    }

    @Test
    void shouldDeleteGiftCertificateById_On_DeleteById() {
        Assertions.assertNotNull(giftCertificateDao.readOne(1));
        giftCertificateTagDao.deleteGiftCertificateTagByCertificateId(1);
        giftCertificateDao.deleteById(1);
        Assertions.assertFalse(giftCertificateDao.readOne(1).isPresent());
    }

    @Test
    void shouldReturnAllGiftCertificates_On_Read() {
        Assertions.assertEquals(oldGiftCertificates,
                giftCertificateDao
                        .readAll(null, null, null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTag_Positive() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificate1),
                giftCertificateDao
                        .readAll("secondTag", null, null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTag_Negative() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll("secondTags", null, null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadName() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificate1),
                giftCertificateDao
                        .readAll(null, "g1", null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadName_Negative() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao.
                        readAll(null, "asd", null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadDescription_Positive() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificate2),
                giftCertificateDao
                        .readAll(null, null, "d2", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadDescription_Negative() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll(null, null, "fg", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadSort_ASC() {
        Assertions.assertEquals(oldGiftCertificates,
                giftCertificateDao
                        .readAll(null, null, null, true, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadSort_DESC() {
        Assertions.assertNotEquals(oldGiftCertificates,
                giftCertificateDao
                        .readAll(null, null, null, false, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndName_Positive() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificate1),
                giftCertificateDao
                        .readAll("secondTag", "1", null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndName_Negative() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll("secondTag", "2", null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndName_Negative_ForTag() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll("secondTag2", "g1", null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndName_Negative_ForName() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll("secondTag", "fgd", null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndName_Negative_ForTagAndName() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll("secoTag", "fgd", null, null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndDescription_Positive() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificate2),
                giftCertificateDao
                        .readAll("firstTag", null, "2", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndDescription_Negative() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll("secondTag", null, "2", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndDescription_Negative_ForTag() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll("secondTag2a", null, "1", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndDescription_Negative_ForDescription() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll("secondTag", null, "dfg", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadTagAndDescription_Negative_ForTagAndDescription() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll("seTaga", null, "asd", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadNameAndDescription_Positive() {
        Assertions.assertEquals(Collections.singletonList(oldGiftCertificate1),
                giftCertificateDao.readAll(null, "1", "1", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadNameAndDescription_Negative() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao.readAll(null, "1", "2", true, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadNameAndDescription_Negative_ForName() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll(null, "eh", "2", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadNameAndDescription_Negative_ForDescription() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll(null, "1", "lkjh", null, null));
    }

    @Test
    void shouldReturnAllGiftCertificates_On_ReadNameAndDescription_Negative_ForNameAndDescription() {
        Assertions.assertEquals(Collections.emptyList(),
                giftCertificateDao
                        .readAll(null, "asd", "asd", null, null));
    }

    @Test
    void shouldUpdateGiftCertificate_On_Update() {
        //Given
        oldGiftCertificate1.setName("newName");
        //When
        giftCertificateDao.update(oldGiftCertificate1);
        final Optional<GiftCertificate> giftCertificate = giftCertificateDao.readOneByName("newName");
        //Then
        Assertions.assertTrue(giftCertificate.isPresent());
        Assertions.assertEquals(oldGiftCertificate1.getName(), giftCertificate.get().getName());
        Assertions.assertEquals(oldGiftCertificate1.getDescription(), giftCertificate.get().getDescription());
        Assertions.assertEquals(oldGiftCertificate1.getPrice(), giftCertificate.get().getPrice());
        Assertions.assertEquals(oldGiftCertificate1.getDuration(), giftCertificate.get().getDuration());
        Assertions.assertEquals(oldGiftCertificate1.getCreateDate(), giftCertificate.get().getCreateDate());
        Assertions.assertNotNull(oldGiftCertificate1.getLastUpdateDate());
    }

    @Test
    void shouldCreateGiftCertificate_On_Create() {
        //When
        final GiftCertificate giftCertificate = giftCertificateDao.create(oldGiftCertificate3);
        //Then
        System.out.println(giftCertificate);
        Assertions.assertEquals(oldGiftCertificate3.getName(), giftCertificate.getName());
        Assertions.assertEquals(oldGiftCertificate3.getDescription(), giftCertificate.getDescription());
        Assertions.assertEquals(oldGiftCertificate3.getPrice(), giftCertificate.getPrice());
        Assertions.assertEquals(oldGiftCertificate3.getDuration(), giftCertificate.getDuration());
        Assertions.assertNotNull(giftCertificate.getCreateDate());
        Assertions.assertNotNull(giftCertificate.getLastUpdateDate());
    }
}
