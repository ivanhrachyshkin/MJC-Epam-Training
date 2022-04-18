package com.epam.esm.view;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.epam.esm.dao", "com.epam.esm.model", "com.epam.esm.view",
        "com.epam.esm.service"})
@EnableJpaRepositories("com.epam.esm.dao")
@EntityScan("com.epam.esm.model")
@EnableConfigurationProperties
@EnableJpaAuditing
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}