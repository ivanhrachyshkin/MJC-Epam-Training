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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends AssertionsProvider<UserDto> {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DtoMapper<User, UserDto> mapper;
    @Mock
    private UserValidator userValidator;
    @Mock
    private PageValidator pageValidator;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private User user;
    @Mock
    private Role userRole;
    @Mock
    private UserDto userDto;
    @Mock
    private Page<User> users;
    @Mock
    private Page<UserDto> dtoUsers;
    @Mock
    private Pageable page;

    private ResourceBundle rb;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(userService, "rb", rb);
    }

    @Test
    void shouldReturnCreatedUser_On_Create() {
        //Given
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(mapper.dtoToModel(userDto)).thenReturn(user);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encoded");
        when(roleRepository.findByRoleName(Role.Roles.ROLE_USER)).thenReturn(userRole);
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.modelToDto(user)).thenReturn(userDto);
        //When
        final UserDto actualDtoUser = userService.create(userDto);
        //Then
        assertEquals(userDto, actualDtoUser);
        verify(userValidator, only()).createValidate(userDto);
        verify(userRepository, times(1)).findByUsername(userDto.getUsername());
        verify(userRepository, times(1)).findByEmail(userDto.getEmail());
        verify(mapper, times(1)).dtoToModel(userDto);
        verify(passwordEncoder, only()).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(mapper, times(1)).modelToDto(user);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowExceptionUsernameExist_On_Create() {
        //Given
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(user));
        final ServiceException expectedException = new ServiceException(
                rb.getString("user.exists.name"),
                HttpStatus.NOT_FOUND, properties.getUser());
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> userService.create(userDto));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(userValidator, only()).createValidate(userDto);
        verify(userRepository, only()).findByUsername(userDto.getUsername());
    }

    @Test
    void shouldThrowExceptionEmailExist_On_Create() {
        //Given
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        final ServiceException expectedException = new ServiceException(
                rb.getString("user.exists.email"),
                HttpStatus.NOT_FOUND, properties.getUser(), user.getUsername());
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> userService.create(userDto));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(userValidator, only()).createValidate(userDto);
        verify(userRepository, times(1)).findByUsername(userDto.getUsername());
        verify(userRepository, times(1)).findByEmail(userDto.getEmail());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldReturnUser_On_ReadOne() {
        //Given
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(mapper.modelToDto(user)).thenReturn(userDto);
        //When
        final UserDto actualDtoUser = userService.readOne(1);
        //Then
        assertEquals(userDto, actualDtoUser);
        verify(userRepository, only()).findById(1);
        verify(mapper, only()).modelToDto(user);
    }

    @Test
    void shouldThrowException_On_ReadOne() {
        //Given
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        final ServiceException expectedException = new ServiceException(
                rb.getString("user.notFound.id"),
                HttpStatus.NOT_FOUND, properties.getUser(), 1);
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> userService.readOne(1));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(userRepository, only()).findById(1);
    }

    @Test
    void shouldReturnUser_On_ReadOneByName() {
        //Given
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(mapper.modelToDto(user)).thenReturn(userDto);
        //When
        final UserDto actualDtoUser = userService.readOneByName(user.getUsername());
        //Then
        assertEquals(userDto, actualDtoUser);
        verify(userRepository, only()).findByUsername(user.getUsername());
        verify(mapper, only()).modelToDto(user);
    }

    @Test
    void shouldThrowException_On_ReadOneByName() {
        //Given
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        final ServiceException expectedException = new ServiceException(
                rb.getString("user.notFound.name"),
                HttpStatus.NOT_FOUND, properties.getUser(), user.getUsername());
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> userService.readOneByName(user.getUsername()));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(userRepository, only()).findByUsername(user.getUsername());
    }

    @Test
    void shouldReturnUsers_On_ReadAll() {
        //Given
        when(userRepository.findAll(page)).thenReturn(users);
        when(mapper.modelsToDto(users)).thenReturn(dtoUsers);
        //When
        final Page<UserDto> actualDtoUsers = userService.readAll(page);
        //Then
        assertPages(dtoUsers, actualDtoUsers);
        verify(pageValidator, only()).paginationValidate(page);
        verify(userRepository, only()).findAll(page);
        verify(mapper, only()).modelsToDto(users);
    }
}
