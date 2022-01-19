package com.epam.esm.integration;

import com.epam.esm.config.ApplicationConfig;
import com.epam.esm.dao.TagDaoImpl;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Sql("/create-tables.sql")
@Sql("/insert-data-tables.sql")
@ActiveProfiles("test")
@WebAppConfiguration
public class GiftCertificateDaoTest {

    @Autowired
    private TagDaoImpl tagDao;

    @org.junit.jupiter.api.Test
    void shouldCreateGiftCertificate2_On_Create_WithOldTag() throws Exception {

        Tag tag = tagDao.readOneByName("firstTag");
        System.out.println(tag);
    }



}
