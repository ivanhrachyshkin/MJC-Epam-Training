package com.epam.esm.service;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.User;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.PaginationValidator;
import com.epam.esm.service.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
    private final PaginationValidator paginationValidator;

    @Override
    @Transactional
    public List<UserDto> readAll(final Integer page, final Integer size) {
        paginationValidator.paginationValidate(page, size);
        final List<User> users = userRepository.readAll(page, size);
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
                .readOne(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("user.notFound.id"), HttpStatus.NOT_FOUND, properties.getUser(), id));
    }
}
