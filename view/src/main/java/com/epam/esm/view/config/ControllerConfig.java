package com.epam.esm.view.config;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.view.hateoas.PagedModelSerializer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.epam.esm.view")
@EnableSpringDataWebSupport
@EnableWebMvc
public class ControllerConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Module jacksonPageWithJsonViewModule() {
        SimpleModule module = new SimpleModule("jackson-page-with-jsonview", Version.unknownVersion());
        module.addSerializer(PagedModel.class, new PagedModelSerializer());
        return module;
    }

    @Profile("keycloak")
    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
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
