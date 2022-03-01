package com.epam.esm.service;

import com.epam.esm.dao.UserRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private DtoMapper<User, UserDto> mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private PageValidator paginationValidator;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @InjectMocks
    private UserServiceImpl userService;

    private ResourceBundle rb;
    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(userService, "rb", rb);

        user = new User();
        user.setId(1);
        user.setEmail("email");

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setEmail("email");
    }

    @Test
    void shouldReturnOrders_On_ReadAll() {
        //Given
        final List<User> users = Collections.singletonList(user);
        final List<UserDto> expectedUsers = Collections.singletonList(userDto);
        when(userRepository.readAll(null, null)).thenReturn(users);
        when(mapper.modelsToDto(users)).thenReturn(expectedUsers);
        //When
        final List<UserDto> actualOrders = userService.readAll(null, null);
        //Then
        assertEquals(expectedUsers, actualOrders);
        verify(userRepository, only()).readAll(null, null);
        verify(paginationValidator, only()).paginationValidate(null, null);
        verify(mapper, only()).modelsToDto(users);
    }

    @Test
    void shouldReturnOrder_On_ReadOne() {
        //When
        when(userRepository.readOne(user.getId())).thenReturn(Optional.of(user));
        when(mapper.modelToDto(user)).thenReturn(userDto);
        final UserDto actual = userService.readOne(userDto.getId());
        //Then
        assertEquals(userDto, actual);
        verify(userRepository, only()).readOne(user.getId());
        verify(mapper, only()).modelToDto(user);
    }

    @Test
    void shouldThrowException_On_ReadOne() {
        //Given
        final String message = String.format(rb.getString("user.notFound.id"), user.getId());
        when(userRepository.readOne(user.getId())).thenReturn(Optional.empty());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> userService.readOne(user.getId()));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(userRepository, only()).readOne(user.getId());
    }
}