//package com.epam.esm.unit.service.config;
//
//
//
//import com.epam.esm.dao.*;
//import com.epam.esm.validator.CreateGiftCertificateValidator;
//import com.epam.esm.validator.CreateTagValidator;
//import com.epam.esm.validator.ReadAllGiftCertificatesValidator;
//import com.epam.esm.validator.UpdateGiftCertificateValidator;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.Clock;
//
//@Configuration
//@ExtendWith(MockitoExtension.class)
//public class TestConfig {
//
//
//
//    @Bean
//    public ReadAllGiftCertificatesValidator readAllGiftCertificatesValidator() {
//        return Mockito.mock(ReadAllGiftCertificatesValidator.class);
//    }
//
//    @Bean
//    public CreateGiftCertificateValidator createGiftCertificateValidator() {
//        return Mockito.mock(CreateGiftCertificateValidator.class);
//    }
//
//    @Bean
//    public UpdateGiftCertificateValidator updateGiftCertificateValidator() {
//        return Mockito.mock(UpdateGiftCertificateValidator.class);
//    }
//
//    @Bean
//    public CreateTagValidator createTagValidator() {
//        return Mockito.mock(CreateTagValidator.class);
//    }
//
//    @Bean
//    public GiftCertificateDao giftCertificateDao() {
//        return Mockito.mock(GiftCertificateDaoImpl.class);
//    }
//
//    @Bean
//    public GiftCertificateTagDao giftCertificateTagDao() {
//        return Mockito.mock(GiftCertificateTagDaoImpl.class);
//    }
//
//    @Bean
//    public TagDao tagDao() {
//        return Mockito.mock(TagDaoImpl.class);
//    }
//
//    @Bean
//    public Clock clock() {
//        return Mockito.mock(Clock.class);
//    }
//
//}
