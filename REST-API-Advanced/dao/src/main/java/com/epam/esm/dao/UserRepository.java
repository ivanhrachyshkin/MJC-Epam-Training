package com.epam.esm.dao;

import com.epam.esm.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> readAll(Integer page, Integer size);

    Optional<User> readOne(int id);
}
