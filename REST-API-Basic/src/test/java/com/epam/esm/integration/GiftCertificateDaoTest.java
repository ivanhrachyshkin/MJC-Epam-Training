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
            1,
            Timestamp.valueOf("2020-02-02 02:02:02.222").toLocalDateTime(),
            Timestamp.valueOf("2020-02-02 02:02:02.222").toLocalDateTime(),
            tags2);

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

//    @Test
//    void shouldDeleteGiftCertificateById_On_DeleteById() {
//        final GiftCertificate giftCertificate1 = giftCertificateDao.readOne(1);
//        Assertions.assertNotNull(giftCertificate1);
//        Assertions.assertNotNull(giftCertificate1.getTags());
//        System.out.println(giftCertificateDao.readAll(null, null, null, null));
//        giftCertificateTagDao.deleteGiftCertificateTagByTagId(1);
//        System.out.println(giftCertificateDao.readAll(null, null, null, null));
//        final GiftCertificate giftCertificate2 = giftCertificateDao.readOne(1);
//       Assertions.assertNull(giftCertificate2.getTags());
//       giftCertificateDao.deleteById(1);
//       Assertions.assertNull(giftCertificateDao.readOne(1));
//    }

//    @Test
//    void shouldFindGiftCertificatesWithTags_On_DeleteById() {
//        final GiftCertificate giftCertificate = giftCertificateDao.readOne(1);
//        Assertions.assertNotNull(giftCertificate);
//        giftCertificateTagDao.deleteGiftCertificateTagByCertificateId(giftCertificate.getId());
//        giftCertificateDao.deleteById(1);
//        giftCertificate.getTags().forEach(tag -> giftCertificateTagDao.deleteGiftCertificateTagByTagId(1));
//        Assertions.assertNull(giftCertificateDao.readOne(1));
//    }


}
