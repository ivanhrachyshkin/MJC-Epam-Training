package com.epam.esm.dao;

import com.epam.esm.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> readAll();

    Optional<User> readOne(int id);

    Optional<User> readOneByEmail(final String email);

}
