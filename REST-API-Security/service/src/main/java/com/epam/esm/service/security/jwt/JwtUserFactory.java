package com.epam.esm.service.security.jwt;

import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class JwtUserFactory {

    public static JwtUser create(UserDto userDto) {
        return new JwtUser(userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getPassword(),
                mapToGrantAuthorities(new ArrayList<>(userDto.getDtoRoles())));
    }

    private static List<GrantedAuthority> mapToGrantAuthorities(List<RoleDto> dtoUserRoles) {
        return dtoUserRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
