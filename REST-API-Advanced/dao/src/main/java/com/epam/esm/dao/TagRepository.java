package com.epam.esm.dao;

import com.epam.esm.model.Tag;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Tag create(Tag tag);

    List<Tag> readAll(Boolean active);

    Optional<Tag> readOne(int id, Boolean active);

    Optional<Tag> readOneByName(String name);

    Optional<Tag> readOneMostUsed();

    Tag update(Tag tag);

    Tag deleteById(int id);
}
