package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.Order;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoMapper implements DtoMapper<User, UserDto> {

    private final ModelMapper modelMapper;

    @Override
    public UserDto modelToDto(final User user) {
        final UserDto dtoUser = modelMapper.map(user, UserDto.class);
        final Set<Order> orders = getOrSetEmptyOrders(user);
        final Set<OrderDto> dtoOrders = orders
                .stream()
                .map(order -> new OrderDto(order.getId()))
                .collect(Collectors.toSet());
        dtoUser.setDtoOrders(dtoOrders);
        final List<Role> roles = getOrSetEmptyRoles(user);
        final List<RoleDto> dtoRoles = roles
                .stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());
        dtoUser.setDtoRoles(dtoRoles);
        return dtoUser;
    }


    @Override
    public User dtoToModel(final UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    @Override
    public Page<UserDto> modelsToDto(final Page<User> users) {
        List<UserDto> collect = users
                .stream()
                .map(this::modelToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(collect, users.getPageable(), users.getTotalElements());
    }

    private Set<Order> getOrSetEmptyOrders(final User user) {
        return user.getOrders() == null ? Collections.emptySet() : user.getOrders();
    }

    private List<Role> getOrSetEmptyRoles(final User user) {
        return user.getRoles() == null ? Collections.emptyList() : user.getRoles();
    }
}
