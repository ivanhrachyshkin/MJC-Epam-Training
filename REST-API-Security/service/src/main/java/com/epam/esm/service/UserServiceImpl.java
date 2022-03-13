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
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.ResourceBundle;

import static com.epam.esm.service.dto.RoleDto.Roles.USER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private Environment env;
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
        userValidator.createValidate(userDto);
        userValidator.validateId(userDto.getId());
        checkExistName(userDto.getUsername());
        checkExistEmail(userDto.getEmail());
        final User user = mapper.dtoToModel(userDto);
        encodePassword(user);
        final Role userRole = roleRepository.findByRoleName(Role.Roles.ROLE_USER);
        user.setRoles(Collections.singletonList(userRole));
        final User savedUser = userRepository.save(user);
        savedUser.setPassword(null);
        return mapper.modelToDto(user);
    }

    @Override
    @Transactional
    public UserDto createKeycloakUser(final UserDto userDto) {
        userValidator.createValidate(userDto);
        userValidator.validateId(userDto.getId());

        final User user = mapper.dtoToModel(userDto);
        final String username = user.getUsername();
        final String email = user.getEmail();
        final String password = user.getPassword();
        final UserRepresentation userRepresentation = setUserParams(username, email, password);

        final Keycloak keycloak = getKeycloak();
        final RealmResource realmResource = keycloak.realm(env.getProperty("keycloak.realm"));
        final UsersResource userResource = realmResource.users();
        final Response response = userResource.create(userRepresentation);

        validateResponse(response);
        mapUserRole(response, realmResource, userResource);
        return new UserDto(username, email, password);
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

    private User checkExist(final int id) {
        userValidator.validateId(id);
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
                            HttpStatus.NOT_FOUND, statusPostfixProperties.getUser());
                });
    }

    private void checkExistEmail(final String email) {
        userRepository
                .findByEmail(email)
                .ifPresent(user -> {
                    throw new ServiceException(
                            rb.getString("user.exists.email"),
                            HttpStatus.NOT_FOUND, statusPostfixProperties.getUser());
                });
    }

    private void encodePassword(final User user) {
        final String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    private Keycloak getKeycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(env.getProperty("keycloak.auth-server-url"))
                .realm(env.getProperty("my-keycloak.admin-realm"))
                .clientId(env.getProperty("my-keycloak.admin-resource"))
                .username(env.getProperty("my-keycloak.admin-username"))
                .password(env.getProperty("my-keycloak.admin-password"))
                .build();
    }

    private void validateResponse(final Response response) {
        if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            throw new ServiceException(
                    rb.getString("user.exist"),
                    HttpStatus.CONFLICT, statusPostfixProperties.getUser());
        }
    }

    private UserRepresentation setUserParams(final String username, final String email, final String password) {
        final CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        final UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(username);
        userRepresentation.setEmail(email);
        userRepresentation.setCredentials(Collections.singletonList(credential));
        userRepresentation.setEnabled(true);

        return userRepresentation;
    }

    private void mapUserRole(final Response response,
                             final RealmResource realmResource,
                             final UsersResource userResource) {
        final String userId = CreatedResponseUtil.getCreatedId(response);
        final RoleRepresentation userRealmRole = realmResource.roles().get(USER).toRepresentation();
        userResource.get(userId).roles().realmLevel().add(Collections.singletonList(userRealmRole));
    }
}
