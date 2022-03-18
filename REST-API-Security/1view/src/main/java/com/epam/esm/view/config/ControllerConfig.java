package com.epam.esm.view.config;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.view.interceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.epam.esm.view")
@EnableSpringDataWebSupport
@EnableWebMvc
public class ControllerConfig implements WebMvcConfigurer {

    @Autowired
    Interceptor myInterceptor;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor);
    }

    @Bean
    public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
        return new HateoasPageableHandlerMethodArgumentResolver();
    }

    @Bean
    public PagedResourcesAssembler<TagDto> pagedResourcesAssemblerTag(
            HateoasPageableHandlerMethodArgumentResolver pageableResolver) {
        return new PagedResourcesAssembler<>(pageableResolver, null);
    }

    @Bean
    public PagedResourcesAssembler<GiftCertificateDto> pagedResourcesAssemblerGift(
            HateoasPageableHandlerMethodArgumentResolver pageableResolver) {
        return new PagedResourcesAssembler<>(pageableResolver, null);
    }

    @Bean
    public PagedResourcesAssembler<OrderDto> pagedResourcesAssemblerOrder(
            HateoasPageableHandlerMethodArgumentResolver pageableResolver) {
        return new PagedResourcesAssembler<>(pageableResolver, null);
    }

    @Bean
    public PagedResourcesAssembler<UserDto> pagedResourcesAssemblerUser(
            HateoasPageableHandlerMethodArgumentResolver pageableResolver) {
        return new PagedResourcesAssembler<>(pageableResolver, null);
    }
}
