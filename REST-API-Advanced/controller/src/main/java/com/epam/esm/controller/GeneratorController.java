package com.epam.esm.controller;


import com.epam.esm.service.GeneratorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/generator", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class GeneratorController {

    private final GeneratorServiceImpl generatorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void generate() {
        generatorService.generate();
    }
}
