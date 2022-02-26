package com.epam.esm.service;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.User;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.PageValidator;
import com.epam.esm.service.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;
    private final DtoMapper<User, UserDto> mapper;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PageValidator paginationValidator;

    @Override
    @Transactional
    public Page<UserDto> readAll(final Pageable pageable) {
        paginationValidator.paginationValidate(pageable);
        final Page<User> users = userRepository.findAll(pageable);
        return mapper.modelsToDto(users);
    }

    @Override
    @Transactional
    public UserDto readOne(final int id) {
        userValidator.validateId(id);
        final User user = checkExist(id);
        return mapper.modelToDto(user);
    }

    private User checkExist(final int id) {
        userValidator.validateId(id);
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("user.notFound.id"), HttpStatus.NOT_FOUND, properties.getUser(), id));
    }
}
