package com.epam.esm.controller.interceptor;

import com.epam.esm.controller.exceptionhandler.RestExceptionHandler;
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
import java.util.ResourceBundle;

@Component
public class Interceptor implements HandlerInterceptor {

    private final GiftCertificateServiceImpl giftCertificateService;
    private final TagServiceImpl tagService;
    private final GiftCertificateValidator giftCertificateValidator;
    private final SortValidator sortValidator;
    private final TagValidator tagValidator;
    private final RestExceptionHandler restExceptionHandler;

    public Interceptor(final GiftCertificateServiceImpl giftCertificateService,
                       final TagServiceImpl tagService,
                       final GiftCertificateValidator giftCertificateValidator,
                       final SortValidator sortValidator, TagValidator tagValidator,
                       final RestExceptionHandler restExceptionHandler) {
        this.giftCertificateService = giftCertificateService;
        this.tagService = tagService;
        this.giftCertificateValidator = giftCertificateValidator;
        this.sortValidator = sortValidator;
        this.tagValidator = tagValidator;
        this.restExceptionHandler = restExceptionHandler;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        final String lang = getLang(request);
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("message", LocaleUtils.toLocale(lang));
        giftCertificateService.setRb(resourceBundle);
        tagService.setRb(resourceBundle);
        giftCertificateValidator.setRb(resourceBundle);
        sortValidator.setRb(resourceBundle);
        tagValidator.setRb(resourceBundle);
        restExceptionHandler.setRb(resourceBundle);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private String getLang(final HttpServletRequest request) {
        String lang = "en_US";
        final String langHeaderValue = request.getHeader("Lang");
        if (langHeaderValue != null) {
            lang = langHeaderValue.equals("ru_RU") ? "ru_RU" : "en_US";
        }
        return lang;
    }
}
