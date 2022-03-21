package com.epam.esm.view.interceptor;

import com.epam.esm.service.*;
import com.epam.esm.service.validator.*;
import com.epam.esm.view.security.jwt.JwtTokenProvider;
import com.epam.esm.view.security.payload.requestvalidator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.LocaleUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class Interceptor implements HandlerInterceptor {

    private final GiftCertificateServiceImpl giftCertificateService;
    private final TagServiceImpl tagService;
    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;
    private final GiftCertificateValidator giftCertificateValidator;
    private final OrderValidator orderValidator;
    private final TagValidator tagValidator;
    private final UserValidator userValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final PageValidator paginationValidator;
    private final RequestValidator requestValidator;
    private final AuthenticatedUserProvider userProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        final String lang = getLang(request);
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("message", LocaleUtils.toLocale(lang));
        giftCertificateService.setRb(resourceBundle);
        tagService.setRb(resourceBundle);
        orderService.setRb(resourceBundle);
        userService.setRb(resourceBundle);
        giftCertificateValidator.setRb(resourceBundle);
        orderValidator.setRb(resourceBundle);
        tagValidator.setRb(resourceBundle);
        userValidator.setRb(resourceBundle);
        paginationValidator.setRb(resourceBundle);
        jwtTokenProvider.setRb(resourceBundle);
        requestValidator.setRb(resourceBundle);
        userProvider.setRb(resourceBundle);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private String getLang(final HttpServletRequest request) {
        String lang = "en_US";
        final String langHeaderValue = request.getHeader("Lang");
        if (langHeaderValue != null && langHeaderValue.equals("ru_RU")) {
            lang = "ru_RU";
        }
        return lang;
    }
}
