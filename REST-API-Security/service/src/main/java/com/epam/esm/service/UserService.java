package com.epam.esm.service;

import com.epam.esm.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    public UserDto create(UserDto userDto);

    Page<UserDto> readAll(Pageable pageable);

    UserDto readOneByName( String name);

    UserDto readOne(int id);
}
