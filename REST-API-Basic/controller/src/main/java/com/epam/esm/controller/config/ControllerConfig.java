package com.epam.esm.controller.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
public class ControllerConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
