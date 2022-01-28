package com.epam.esm.controller.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
public class ControllerConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("message", Locale.US);
    }
}
