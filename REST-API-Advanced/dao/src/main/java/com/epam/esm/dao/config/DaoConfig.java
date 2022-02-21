package com.epam.esm.dao.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.Random;

@Configuration
public class DaoConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
