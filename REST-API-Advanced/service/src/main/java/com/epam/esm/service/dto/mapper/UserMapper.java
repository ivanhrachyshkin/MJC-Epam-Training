package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper implements DtoMapper<User, UserDto> {

    private final ModelMapper modelMapper;

    @Override
    public UserDto modelToDto(final User user) {
        emptyTagsIfNull(user);
        final Set<OrderDto> dtoOrders = user
             .getOrders()
                .stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toSet());
        final UserDto dtoUser = modelMapper.map(user, UserDto.class);
        dtoUser.setOrders(dtoOrders);
        return dtoUser;
    }

    @Override
    public User dtoToModel(final UserDto userDto) {
        final User user = modelMapper.map(userDto, User.class);
        emptyTagsIfNull(user);
        final Set<Order> orders = user
                .getOrders()
                .stream()
                .map(orderDto -> modelMapper.map(orderDto, Order.class))
                .collect(Collectors.toSet());
        user.setOrders(orders);
        return user;
    }

    @Override
    public List<UserDto> modelsToDto(List<User> users) {
        return users
                .stream()
                .map(this::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> dtoToModels(List<UserDto> dtoUsers) {
        return dtoUsers
                .stream()
                .map(this::dtoToModel)
                .collect(Collectors.toList());
    }

    private void emptyTagsIfNull(final User user) {
        if (user.getOrders() == null) {
            user.setOrders(Collections.emptySet());
        }
    }
}
