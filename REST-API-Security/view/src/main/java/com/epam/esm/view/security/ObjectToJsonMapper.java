package com.epam.esm.view.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ObjectToJsonMapper {

    public String convertObjectToJson(final Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        final ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
