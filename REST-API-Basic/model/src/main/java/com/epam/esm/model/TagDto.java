package com.epam.esm.model;

import java.util.Objects;

public class TagDto {

    private Integer id;
    private String name;

    public TagDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagDto() {
    }

    public TagDto(final String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDto tag = (TagDto) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
