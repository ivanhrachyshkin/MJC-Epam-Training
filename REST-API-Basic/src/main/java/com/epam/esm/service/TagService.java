package com.epam.esm.service;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagService {

    Tag create(Tag tag);

    List<Tag> readAll();

    Tag readOne(int id);

    void deleteById(int id);
}
