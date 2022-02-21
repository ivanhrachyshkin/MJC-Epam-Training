package com.epam.esm.dao;

import com.epam.esm.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Tag create(Tag tag);

    List<Tag> readAll(Boolean active, Integer page, Integer size);

    Optional<Tag> readOne(int id, Boolean active);

    Optional<Tag> readOneByName(String name);

    List<Tag> readMostUsed();

    Optional<Tag> deleteById(int id);
}
