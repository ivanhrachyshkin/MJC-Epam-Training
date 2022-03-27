package com.epam.esm.service;

import com.epam.esm.dao.RoleRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.Role;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties statusPostfixProperties;
    private final DtoMapper<User, UserDto> mapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidator userValidator;
    private final PageValidator paginationValidator;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public UserDto create(final UserDto userDto) {
        userDto.trim();
        userValidator.createValidate(userDto);
        checkExistName(userDto.getUsername());
        checkExistEmail(userDto.getEmail());
        final User user = mapper.dtoToModel(userDto);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        final Role userRole = roleRepository.findByRoleName(Role.Roles.ROLE_USER);
        user.setRoles(Collections.singletonList(userRole));
        final User savedUser = userRepository.save(user);
        return mapper.modelToDto(savedUser);
    }

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

    @Override
    @Transactional
    public UserDto readOneByName(final String name) {
        final User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("user.notFound.name"),
                        HttpStatus.NOT_FOUND, statusPostfixProperties.getUser(), name));
        return mapper.modelToDto(user);
    }

    public User checkExist(final int id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("user.notFound.id"),
                        HttpStatus.NOT_FOUND, statusPostfixProperties.getUser(), id));
    }

    private void checkExistName(final String name) {
        userRepository
                .findByUsername(name)
                .ifPresent(user -> {
                    throw new ServiceException(
                            rb.getString("user.exists.name"),
                            HttpStatus.CONFLICT, statusPostfixProperties.getUser());
                });
    }

    private void checkExistEmail(final String email) {
        userRepository
                .findByEmail(email)
                .ifPresent(user -> {
                    throw new ServiceException(
                            rb.getString("user.exists.email"),
                            HttpStatus.CONFLICT, statusPostfixProperties.getUser());
                });
    }
}
