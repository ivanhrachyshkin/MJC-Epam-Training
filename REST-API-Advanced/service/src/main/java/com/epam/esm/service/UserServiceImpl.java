package com.epam.esm.service;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.TagValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TagValidator tagValidator;
    private final DtoMapper<User, UserDto> mapper;


    @Override
    @Transactional
    public List<UserDto> readAll() {
        final List<User> users = userRepository.readAll();
        return mapper.modelsToDto(users);
    }

    @Override
    @Transactional
    public UserDto readOne(final int id) {
        final User user = checkExist(id);
        return mapper.modelToDto(user);
    }

    private User checkExist(final int id) {
        return userRepository
                .readOne(id)
                .orElseThrow(() -> new ServiceException("tag.notFound.id", id));
    }
}
