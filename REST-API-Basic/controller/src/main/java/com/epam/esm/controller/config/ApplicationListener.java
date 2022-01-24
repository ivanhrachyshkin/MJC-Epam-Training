package com.epam.esm.controller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationListener {

    private final String message;

    public ApplicationListener(@Value("${message}") final String message) {
        this.message = message;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        System.out.println(message);
    }
}
