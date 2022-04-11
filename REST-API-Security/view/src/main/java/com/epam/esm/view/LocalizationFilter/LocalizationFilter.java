package com.epam.esm.view.LocalizationFilter;

import com.epam.esm.service.*;
import com.epam.esm.service.validator.*;
import com.epam.esm.view.exceptionhandler.AccessDeniedExceptionHandler;
import com.epam.esm.view.exceptionhandler.RestAuthenticationEntryPoint;
import com.epam.esm.view.exceptionhandler.RestExceptionHandler;
import com.epam.esm.view.security.payload.requestvalidator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.LocaleUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class LocalizationFilter extends GenericFilterBean {

    private final GiftCertificateServiceImpl giftCertificateService;
    private final TagServiceImpl tagService;
    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;
    private final GiftCertificateValidator giftCertificateValidator;
    private final OrderValidator orderValidator;
    private final TagValidator tagValidator;
    private final UserValidator userValidator;
    private final PageableValidator paginationValidator;
    private final RequestValidator requestValidator;
    private final AuthenticatedUserProvider userProvider;
    private final RestExceptionHandler restExceptionHandler;
    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RefreshTokenServiceImpl refreshTokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String lang = getLang((HttpServletRequest) request);
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
        requestValidator.setRb(resourceBundle);
        userProvider.setRb(resourceBundle);
        restExceptionHandler.setRb(resourceBundle);
        refreshTokenService.setRb(resourceBundle);
        accessDeniedExceptionHandler.setRb(resourceBundle);
        restAuthenticationEntryPoint.setRb(resourceBundle);
        chain.doFilter(request, response);
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
