package com.epam.esm.dao;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDao {

    Integer create(Tag tag);

    List<Tag> readAll();

    Tag readOne(int id);

    Tag readOneByName(final String name);

    void deleteById(int id);
}
