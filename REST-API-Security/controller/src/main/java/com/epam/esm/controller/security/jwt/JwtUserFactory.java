package com.epam.esm.controller.security.jwt;

import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class JwtUserFactory {

    public static UserDetails create(UserDto userDto) {
        return new JwtUser(userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getPassword(),
                mapToGrantAuthorities(new ArrayList<>(userDto.getDtoRoles())));
    }

    private static List<GrantedAuthority> mapToGrantAuthorities(List<RoleDto> dtoRoles) {
        return dtoRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority(String.valueOf(role.getRoleName())))
                .collect(Collectors.toList());
    }
}
