package com.epam.esm.service;

import com.epam.esm.dao.GeneratorRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeneratorServiceImplTest {

    @Mock
    private GeneratorRepositoryImpl generatorRepository;
    @InjectMocks
    private GeneratorServiceImpl generatorService;

    @Test
    void shouldPath_On_Generate() {
        doNothing().when(generatorRepository).generate();
        generatorService.generate();
        verify(generatorRepository, only()).generate();
    }
}