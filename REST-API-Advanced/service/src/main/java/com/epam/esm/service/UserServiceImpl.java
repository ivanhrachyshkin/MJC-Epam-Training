package com.epam.esm.service;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ResourceBundle rb;
    private final UserRepository userRepository;
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
                .orElseThrow(() -> new ServiceException(rb.getString("user.notFound.id"), id));
    }
}
