package com.epam.esm.controller;

import com.epam.esm.service.GiftCertificateServiceImpl;
import com.epam.esm.service.TagServiceImpl;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.SortValidator;
import com.epam.esm.service.validator.TagValidator;
import org.apache.commons.lang3.LocaleUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class MyInterceptor implements HandlerInterceptor {

    private final GiftCertificateServiceImpl giftCertificateService;
    private final TagServiceImpl tagService;
    private final GiftCertificateValidator giftCertificateValidator;
    private final SortValidator sortValidator;
    private final TagValidator tagValidator;

    public MyInterceptor(GiftCertificateServiceImpl giftCertificateService,
                         TagServiceImpl tagService,
                         GiftCertificateValidator giftCertificateValidator,
                         SortValidator sortValidator, TagValidator tagValidator) {
        this.giftCertificateService = giftCertificateService;
        this.tagService = tagService;
        this.giftCertificateValidator = giftCertificateValidator;
        this.sortValidator = sortValidator;
        this.tagValidator = tagValidator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String lang = request.getHeader("Lang");
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("message", LocaleUtils.toLocale(lang));
        System.out.println(lang);
        giftCertificateService.setRb(resourceBundle);
        tagService.setRb(resourceBundle);
        giftCertificateValidator.setRb(resourceBundle);
        sortValidator.setRb(resourceBundle);
        tagValidator.setRb(resourceBundle);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
