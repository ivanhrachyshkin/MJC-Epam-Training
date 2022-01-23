package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.service.validator.CreateTagValidator;
import com.epam.esm.service.validator.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {

    @Mock
    private CreateTagValidator createTagValidator;

    @Test
    void shouldCreateTag_On_Create() {

    }
}