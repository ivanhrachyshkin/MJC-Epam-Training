package com.epam.esm.controller.config;

import com.epam.esm.controller.interceptor.Interceptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
public class ControllerConfig implements WebMvcConfigurer {

    @Autowired
    Interceptor myInterceptor;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        //final MyInterceptor myInterceptor = new MyInterceptor();
        registry.addInterceptor(myInterceptor);
    }
}
