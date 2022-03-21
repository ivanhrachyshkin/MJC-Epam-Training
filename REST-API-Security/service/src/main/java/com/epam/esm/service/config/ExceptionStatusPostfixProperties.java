package com.epam.esm.service.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "error")
@Getter
@Setter
@NoArgsConstructor
public class ExceptionStatusPostfixProperties {

    private String gift;
    private String tag;
    private String user;
    private String order;
    private String pagination;
    private String auth;
    private String req;
}