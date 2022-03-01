package com.epam.esm.service;

import com.epam.esm.dao.GeneratorRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl {

    private final GeneratorRepositoryImpl generatorRepository;

    @Transactional
    public void generate () {
        generatorRepository.generate();
    }
}
