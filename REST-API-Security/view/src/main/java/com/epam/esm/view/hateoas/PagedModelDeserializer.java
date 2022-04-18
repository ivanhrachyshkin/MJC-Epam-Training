package com.epam.esm.view.hateoas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedModelDeserializer<T> {

    private List<T> content;

    public List<T> getContent() {
        return content;
    }
}
