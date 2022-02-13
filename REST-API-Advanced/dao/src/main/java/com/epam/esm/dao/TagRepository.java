package com.epam.esm.dao;

import com.epam.esm.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Tag create(Tag tag);

    List<Tag> readAll();

    Optional<Tag> readOne(int id);

    Optional<Tag> readOneByName(final String name);

    Optional<Tag> readOneMostUsed();

    Tag deleteById(int id);
}
